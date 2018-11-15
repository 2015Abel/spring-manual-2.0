package com.demo.abel.framework.bean;

/**
 * @description: instance上的又一层包装，提供代理
 * @author: liuzijian
 * @create: 2018-11-15 22:09
 **/
public class BeanWrapper {
    public BeanWrapper(Object instance) {
        this.instance = instance;
    }

    private Object instance;

    public Object getWrappedInstance(){
        return instance;
    }
}
