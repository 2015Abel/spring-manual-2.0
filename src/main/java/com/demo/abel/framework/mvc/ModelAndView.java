package com.demo.abel.framework.mvc;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @description: TODO 类描述
 * @author: liuzijian
 * @create: 2018-11-22 22:56
 **/
@Setter
@Getter
public class ModelAndView {
    private String viewName;
    private Map<String,Object> model;
}
