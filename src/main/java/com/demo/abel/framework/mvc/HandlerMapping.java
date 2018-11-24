package com.demo.abel.framework.mvc;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @description: TODO 类描述
 * @author: liuzijian
 * @create: 2018-11-22 00:29
 **/
@Setter
@Getter
public class HandlerMapping{
    private Pattern url;
    private Object controller;
    private Method method;
}
