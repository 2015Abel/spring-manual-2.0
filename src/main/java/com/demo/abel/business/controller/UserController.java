package com.demo.abel.business.controller;

import com.demo.abel.business.service.UserService;
import com.demo.abel.framework.annotation.Autowire;
import com.demo.abel.framework.annotation.Qualifier;
import com.demo.abel.framework.annotation.auto.clz.Controller;
import com.demo.abel.framework.annotation.mvc.RequestMapping;
import com.demo.abel.framework.annotation.mvc.RequestParam;
import com.demo.abel.framework.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: UserController TODO
 * @author: liuzijian
 * @create: 2018-11-15 10:13
 **/
@Controller
@RequestMapping(url = "user")
public class UserController {
    @Autowire
    @Qualifier(name = "aliasUserService")
    private UserService aliasUserService;

    @RequestMapping(url = "toHello")
    public ModelAndView toHello(HttpServletRequest request,
                                @RequestParam("bookId")Long bookId,
                                HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hello.html");
        Map<String,Object> model = new HashMap<>();
        model.put("bookName",aliasUserService.speakBookName(bookId));
        model.put("name","Abel");
        model.put("words","Hello world!");
        modelAndView.setModel(model);
        return modelAndView;
    }

}
