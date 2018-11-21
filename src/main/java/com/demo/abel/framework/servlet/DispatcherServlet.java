package com.demo.abel.framework.servlet;

import com.demo.abel.framework.core.ApplicationContext;

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

        ApplicationContext context = new ApplicationContext(config);

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
    protected void initStrategies(ApplicationContext context){
        initHandlerMappings(context);
        initHandlerAdapters(context);
        initViewResolvers(context);
    }

    private void initViewResolvers(ApplicationContext context) {
    }

    private void initHandlerAdapters(ApplicationContext context) {
    }

    private void initHandlerMappings(ApplicationContext context) {
    }
}
