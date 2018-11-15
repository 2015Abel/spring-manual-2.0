package com.demo.abel.business.controller;

import com.demo.abel.business.service.UserService;
import com.demo.abel.framework.annotation.Autowire;
import com.demo.abel.framework.annotation.auto.clz.Controller;
import com.demo.abel.framework.annotation.Qualifier;

/**
 * @description: UserController
 * @author: liuzijian
 * @create: 2018-11-15 10:13
 **/
@Controller
public class UserController {
    @Autowire
    @Qualifier(name = "aliasUserService")
    private UserService aliasUserService;

}
