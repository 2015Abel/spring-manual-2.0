package com.demo.abel.framework.servlet;

import com.demo.abel.framework.context.AnnotationApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description: 框架入口
 * @author: liuzijian
 * @create: 2018-11-13 23:27
 **/
public class DispatcherServlet extends HttpServlet {

    ServletConfig config;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
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
    protected void initStrategies(AnnotationApplicationContext context){
        initHandlerMappings(context);
        initHandlerAdapters(context);
        initViewResolvers(context);
    }

    private void initViewResolvers(AnnotationApplicationContext context) {
    }

    private void initHandlerAdapters(AnnotationApplicationContext context) {
    }

    private void initHandlerMappings(AnnotationApplicationContext context) {
    }
}
