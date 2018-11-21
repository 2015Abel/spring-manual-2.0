package com.demo.abel.framework.context.support;

import com.demo.abel.framework.annotation.RequestMapping;
import com.demo.abel.framework.annotation.auto.clz.Controller;
import com.demo.abel.framework.context.AnnotationApplicationContext;
import com.sun.istack.internal.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: springMVC初始化支持
 * @author: liuzijian
 * @create: 2018-11-21 23:37
 **/
public class ApplicationObjectSupport {

    private final Map<String,Object> handlerMap = new ConcurrentHashMap<>(64);

    @NotNull
    AnnotationApplicationContext context;

    public final void setApplicationContext(AnnotationApplicationContext context){
        this.context = context;
        registryHandler();
    }

    void registryHandler(){
        List<String> beanNames = context.getBeanNamesForType(Object.class);
        for (String name:beanNames){
            Object bean = context.getBean(name);
            Class beanClass = bean.getClass();
            if(beanClass.isAnnotationPresent(Controller.class)){
                Object controller = bean;
                RequestMapping requestMapping = (RequestMapping) beanClass.getAnnotation(RequestMapping.class);
                handlerMap.put(requestMapping.url(),controller);
            }
        }
    }
}
