package com.demo.abel.framework.mvc;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

/**
 * @description: 视图解析
 * @author: liuzijian
 * @date: 2018-11-22 11:18
 */
@Setter
@Getter
public class ViewResolver {
    private String viewName;
    private String fullPath;
    private File template;

    public String resove(ModelAndView modelAndView){
        return null;
    }
}
