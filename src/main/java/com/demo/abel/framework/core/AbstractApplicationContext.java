package com.demo.abel.framework.core;

import com.demo.abel.framework.core.service.BeanFactory;

import java.util.Properties;

/**
 * @description: refresh主逻辑
 *
 * @author: liuzijian
 * @date: 2018-11-14 11:39
 */
public abstract class AbstractApplicationContext implements BeanFactory {

    protected Properties config = new Properties();


    @Override
    public Object getBean(String name) {
        return null;
    }

    public void refresh(){
        loadBeanDefinitions();
    }

    private void loadBeanDefinitions(){

    }


}
