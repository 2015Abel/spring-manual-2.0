package com.demo.abel.business.controller;

import com.demo.abel.business.service.UserService;
import com.demo.abel.framework.annotation.Autowire;
import com.demo.abel.framework.annotation.mvc.ReponseBody;
import com.demo.abel.framework.annotation.mvc.RequestMapping;
import com.demo.abel.framework.annotation.auto.clz.Controller;
import com.demo.abel.framework.annotation.Qualifier;
import com.demo.abel.framework.annotation.mvc.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: UserController
 * @author: liuzijian
 * @create: 2018-11-15 10:13
 **/
@Controller
@RequestMapping(url = "user")
public class UserController {
    @Autowire
    @Qualifier(name = "aliasUserService")
    private UserService aliasUserService;

    @ReponseBody
    @RequestMapping(url = "doSomething")
    public Object doSomething(@RequestParam("actionType")String actionType,
                              @RequestParam("linkId")Long bookId,
                              HttpServletRequest request, HttpServletResponse response){
        return null;
    }

}
