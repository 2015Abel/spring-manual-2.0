package com.demo.abel.framework.servlet;

import com.demo.abel.framework.annotation.auto.clz.Controller;
import com.demo.abel.framework.annotation.mvc.RequestMapping;
import com.demo.abel.framework.constant.AbelConstants;
import com.demo.abel.framework.context.AbstractApplicationContext;
import com.demo.abel.framework.context.AnnotationApplicationContext;
import com.demo.abel.framework.mvc.HandlerMapping;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @description: 框架入口
 * @author: liuzijian
 * @create: 2018-11-13 23:27
 **/
public class DispatcherServlet extends HttpServlet {

    ServletConfig config;
    List<HandlerMapping> handlerMappings = new LinkedList<>();


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

    private void initHandlerMappings(AbstractApplicationContext context) {
        List<String> beanNames = context.getBeanNamesForType(Object.class);
        for(String name:beanNames){
            Object bean = context.getBean(name);
            if(!bean.getClass().isAnnotationPresent(Controller.class)){
                continue;
            }

            RequestMapping requestMapping = bean.getClass().getAnnotation(RequestMapping.class);
            if(requestMapping==null){
                //TODO 简易版，暂时如此
                throw new RuntimeException("controller 必须被 @RequestMapping修饰！");
            }

            String url = requestMapping.url();
            if(!url.startsWith(AbelConstants.URL_PREFIX)){
                url+=AbelConstants.URL_PREFIX;
            }

            HandlerMapping handlerMapping = new HandlerMapping();
            handlerMapping.setUrl(Pattern.compile(url+"*"));
            handlerMapping.setController(bean);
            //TODO method map

        }
    }

    private void initHandlerAdapters(AbstractApplicationContext context) {
    }

    private void initViewResolvers(AbstractApplicationContext context) {

    }
}
