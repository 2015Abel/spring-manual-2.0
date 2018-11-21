package com.demo.abel.framework.context.service;

import java.util.List;

/**
 * @description: BeanFactory
 * @author: liuzijian
 * @create: 2018-11-13 23:45
 **/
public interface BeanFactory {
    Object getBean(String name);
    List<String> getBeanNamesForType(Class<?> clz);
}
