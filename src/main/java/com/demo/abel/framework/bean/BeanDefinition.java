package com.demo.abel.framework.bean;

import lombok.Getter;

/**
 * @description: 封装bean
 * @author: liuzijian
 * @create: 2018-11-15 10:52
 **/
@Getter
public class BeanDefinition {

    private Class beanClass;

    public BeanDefinition(Class beanClass) {
        this.beanClass = beanClass;
    }

    public String getBeanClassName(){
        return beanClass.getName();
    }

    public boolean isLazyInit(){
        return false;
    }

    public boolean isSingleton(){
        return true;
    }
}
