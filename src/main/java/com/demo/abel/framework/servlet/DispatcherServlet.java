package com.demo.abel.framework.servlet;

import com.demo.abel.framework.annotation.auto.clz.Controller;
import com.demo.abel.framework.annotation.mvc.RequestMapping;
import com.demo.abel.framework.annotation.mvc.RequestParam;
import com.demo.abel.framework.constant.AbelConstants;
import com.demo.abel.framework.context.AbstractApplicationContext;
import com.demo.abel.framework.context.AnnotationApplicationContext;
import com.demo.abel.framework.mvc.HandlerAdapter;
import com.demo.abel.framework.mvc.HandlerMapping;
import com.demo.abel.framework.mvc.ModelAndView;
import com.demo.abel.framework.util.CollectionUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @description: 框架入口
 * @author: liuzijian
 * @create: 2018-11-13 23:27
 **/
public class DispatcherServlet extends HttpServlet {

    ServletConfig config;

    List<HandlerMapping> handlerMappings = new LinkedList<>();
    Map<HandlerMapping,HandlerAdapter> handlerAdapters = new HashMap<>();
    Map<String,ModelAndView> viewResolvers = new HashMap<>();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    public void init() throws ServletException {
        config = getServletConfig();

        AnnotationApplicationContext context = new AnnotationApplicationContext(config);

        /**
         * IOC 测试
         */
//        UserService userService = (UserService) context.getBean("aliasUserService");
//        userService.readABook();
//        UserServiceImpl uImpl = (UserServiceImpl) context.getBean("userService");
//        UserServiceImpl wImpl = (UserServiceImpl) context.getBean("workService");
//        UserController userController = (UserController) context.getBean("userController");

        initStrategies(context);
    }

    /**
     * 初始化3大组件
     * @param context
     */
    protected void initStrategies(AbstractApplicationContext context){
        initHandlerMappings(context);
        initHandlerAdapters(context);
        initViewResolvers(context);
    }

    /**
     * 初始化handlerMappings，关联url至类和方法
     * @param context
     */
    private void initHandlerMappings(AbstractApplicationContext context) {
        List<String> beanNames = context.getBeanNamesForType(Object.class);
        for(String name:beanNames){
            Object bean = context.getBean(name);
            if(!bean.getClass().isAnnotationPresent(Controller.class)){
                continue;
            }

            RequestMapping controllerUrlAnnotation = bean.getClass().getAnnotation(RequestMapping.class);
            String controllerUrl;
            if(controllerUrlAnnotation==null){
                controllerUrl = AbelConstants.URL_PREFIX;
            }else {
                controllerUrl = controllerUrlAnnotation.url();
            }

            if(!controllerUrl.startsWith(AbelConstants.URL_PREFIX)){
                controllerUrl = AbelConstants.URL_PREFIX + controllerUrl;
            }
            if(!controllerUrl.endsWith(AbelConstants.URL_PREFIX)){
                controllerUrl = controllerUrl + AbelConstants.URL_PREFIX;
            }

            HandlerMapping handlerMapping = new HandlerMapping();

            handlerMapping.setUrl(Pattern.compile(controllerUrl+"(*)"));
            handlerMapping.setController(bean);
            //method map
            Map<String,Method> methodMap = new HashMap<>();
            Method[] methods = bean.getClass().getMethods();
            for(Method method:methods){
                RequestMapping methodUrlAnnotation = method.getAnnotation(RequestMapping.class);
                if(methodUrlAnnotation==null){
                    continue;
                }

                String url = methodUrlAnnotation.url();
                methodMap.put(url,method);
            }
            handlerMapping.setMethodMap(methodMap);
            handlerMappings.add(handlerMapping);
        }
    }

    /**
     * 初始化handlerAdapters，方法参数定位
     * xxx?b=xxb&a=xxa
     * @param context
     */
    private void initHandlerAdapters(AbstractApplicationContext context) {
        for(HandlerMapping hm:handlerMappings){
            if(CollectionUtils.isEmpty(hm.getMethodMap())){
                continue;
            }
            for(Method method:hm.getMethodMap().values()){
                Parameter[] pms = method.getParameters();
                if(pms==null || pms.length==0){
                    continue;
                }

                HandlerAdapter handlerAdapter = new HandlerAdapter();
                for (int i=0,len=pms.length;i<len;i++){
                    if(pms[i].getType() == HttpServletRequest.class || pms[i].getType() == HttpServletResponse.class){
                        handlerAdapter.setPosition(pms[i].getType().getSimpleName(),i);
                        continue;
                    }

                    RequestParam requestParam;
                    if ((requestParam = pms[i].getAnnotation(RequestParam.class))!=null){
                        handlerAdapter.setPosition(requestParam.value(),i);
                    }else {
                        //只支持 @RequestParam 参数绑定
                        throw new RuntimeException("The parameter of controller method need to annotationed by @RequestParam!");
                    }
                }
                handlerAdapters.put(hm,handlerAdapter);
            }
        }
    }

    private void initViewResolvers(AbstractApplicationContext context) {
        String templatePath = context.getConfigProperty(AbelConstants.TEMPLATE_PATH);
        try {
            URI uri = getClass().getClassLoader().getResource(templatePath).toURI();
            File template = new File(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
