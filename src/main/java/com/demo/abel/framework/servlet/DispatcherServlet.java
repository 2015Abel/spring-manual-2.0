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
import com.demo.abel.framework.mvc.ViewResolver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
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
    Map<String,ViewResolver> viewResolvers = new HashMap<>();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestPath = req.getRequestURI();

        HandlerMapping handlerMapping = getHandlerMapping(requestPath);
        if(handlerMapping==null){
            throw new RuntimeException("404 Exception！");
        }

        HandlerAdapter handlerAdapter = getHandlerAdapter(handlerMapping);

        ModelAndView mv = handlerAdapter.handle(req,resp,handlerMapping);
        String viewName = mv.getViewName();
        ViewResolver viewResolver = viewResolvers.get(viewName);
        if(viewResolver==null){
            throw new RuntimeException("can not find any view resolver named `"+viewName+"`");
        }

        String result = viewResolver.resolve(mv);
        PrintWriter writer = resp.getWriter();
        writer.write(result);
        writer.flush();
        writer.close();
    }

    private HandlerAdapter getHandlerAdapter(HandlerMapping handlerMapping){
        return handlerAdapters.get(handlerMapping);
    }

    private HandlerMapping getHandlerMapping(String requestPath){
        HandlerMapping result = null;
        for(HandlerMapping handlerMapping:handlerMappings){
            Matcher matcher = handlerMapping.getUrl().matcher(requestPath);
            if(matcher.matches()){
                result = handlerMapping;
            }
        }
        return result;
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


            Method[] methods = bean.getClass().getMethods();
            for(Method method:methods){
                RequestMapping methodUrlAnnotation = method.getAnnotation(RequestMapping.class);
                if(methodUrlAnnotation==null){
                    continue;
                }

                String url = controllerUrl+methodUrlAnnotation.url();
                url = url.replaceAll("/+","/");

                HandlerMapping handlerMapping = new HandlerMapping();

                handlerMapping.setUrl(Pattern.compile(url));
                handlerMapping.setController(bean);
                handlerMapping.setMethod(method);
                handlerMappings.add(handlerMapping);
            }
        }
    }

    /**
     * 初始化handlerAdapters，方法参数定位
     * xxx?b=xxb&a=xxa
     * @param context
     */
    private void initHandlerAdapters(AbstractApplicationContext context) {
        for(HandlerMapping hm:handlerMappings){
            Method method = hm.getMethod();
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

    private String templatePath;

    /**
     * 初始化视图
     * @param context
     */
    private void initViewResolvers(AbstractApplicationContext context) {
        templatePath = context.getConfigProperty(AbelConstants.TEMPLATE_PATH);
        initViewResolves(templatePath);
    }

    private void initViewResolves(String path){
        try {
            URI uri = getClass().getClassLoader().getResource(path).toURI();
            File template = new File(uri);
            if(!template.exists()){
                throw new RuntimeException("Can not find any template from path `"+uri.getPath()+"`");
            }

            File[] templates = template.listFiles();
            for(File file:templates){
                if(file.isDirectory()){
                    path = path + File.separator + file.getName();
                    initViewResolves(path);
                }else {
                    ViewResolver vr = new ViewResolver();
                    vr.setTemplate(file);
                    vr.setViewName(file.getName());
                    String fullPath = path + AbelConstants.URL_PREFIX + file.getName();
                    fullPath = fullPath.replace(templatePath,"");
                    if(fullPath.startsWith(AbelConstants.URL_PREFIX)){
                        fullPath = fullPath.substring(1);
                    }
                    vr.setFullPath(fullPath);
                    viewResolvers.put(vr.getFullPath(),vr);
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
