package com.demo.abel.framework.core;

import com.demo.abel.framework.annotation.Autowire;
import com.demo.abel.framework.annotation.Qualifier;
import com.demo.abel.framework.annotation.auto.clz.Controller;
import com.demo.abel.framework.annotation.auto.clz.Repository;
import com.demo.abel.framework.annotation.auto.clz.Service;
import com.demo.abel.framework.bean.BeanDefinition;
import com.demo.abel.framework.bean.BeanWrapper;
import com.demo.abel.framework.constant.AbelConstants;
import com.demo.abel.framework.core.handler.BeanDefinitionReader;
import com.demo.abel.framework.core.service.BeanFactory;
import com.demo.abel.framework.util.StringUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @description: refresh主逻辑
 *
 * @author: liuzijian
 * @date: 2018-11-14 11:39
 */
public abstract class AbstractApplicationContext implements BeanFactory {

    protected Properties config = new Properties();
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
    private final ConcurrentMap<String, BeanWrapper> beanWrapperMap = new ConcurrentHashMap<>();
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    @Override
    public Object getBean(String name) {
        BeanDefinition definition = beanDefinitionMap.get(name);
        Object instance = null;
        if(definition.isSingleton()){
            instance = singletonObjects.get(name);
            if(instance==null){
                BeanWrapper beanWrapper = beanWrapperMap.get(name);
                if(beanWrapper==null){
                    try {
                        instance = definition.getBeanClass().newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    beanWrapper = new BeanWrapper(instance);
                    beanWrapperMap.putIfAbsent(name,beanWrapper);
                }
                instance = beanWrapper.getWrappedInstance();
                singletonObjects.put(name,instance);
            }
        }
        return instance;
    }

    public void refresh(){
        //加载/注册
        loadBeanDefinitions();

        //初始化bean
        init();
    }

    private void init(){
        if(beanDefinitionMap.size()==0){
            return;
        }

        for(Map.Entry<String,BeanDefinition> entry:beanDefinitionMap.entrySet()){
            String beanClassName = entry.getKey();
            BeanDefinition definition = entry.getValue();
            if(definition.isLazyInit()){
                continue;
            }

            Object instance = getBean(beanClassName);
        }
        for (Map.Entry<String,BeanWrapper> entry: beanWrapperMap.entrySet()){
            populateBean(entry.getValue().getWrappedInstance());
        }
    }

    private void populateBean(Object instance) {
        Class clz = instance.getClass();
        Field[] fields = clz.getDeclaredFields();
        for (Field field:fields){
            String beanClassName = null;
            if(field.isAnnotationPresent(Qualifier.class)){
                Qualifier qualifier = field.getAnnotation(Qualifier.class);
                beanClassName = qualifier.name();
            }else if(field.isAnnotationPresent(Autowire.class)){
                beanClassName = StringUtil.lowerFirstLetter(field.getType().getSimpleName());
            }

            field.setAccessible(true);
            try {
                field.set(instance, beanWrapperMap.get(beanClassName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    private void loadBeanDefinitions(){
        String rootPath = config.getProperty(AbelConstants.ROOT_PATH);
        new BeanDefinitionReader(this).loadBeanDefinitions(rootPath);
    }

    public void registy(BeanDefinition definition){
        Class beanClass = definition.getBeanClass();
        String key;
        if(beanClass.isAnnotationPresent(Controller.class)){
            Controller controller = (Controller) beanClass.getAnnotation(Controller.class);
            if(!StringUtil.isEmpty(controller.name())){
                key = controller.name();
            }else {
                key = StringUtil.lowerFirstLetter(beanClass.getSimpleName());
            }
            beanDefinitionMap.put(key,definition);
        }else if(beanClass.isAnnotationPresent(Repository.class)){
            Repository repository = (Repository) beanClass.getAnnotation(Repository.class);
            if(!StringUtil.isEmpty(repository.name())){
                key = repository.name();
            }else {
                key = StringUtil.lowerFirstLetter(beanClass.getSimpleName());
            }
            beanDefinitionMap.put(key,definition);
        }else if(beanClass.isAnnotationPresent(Service.class)){
            Service service = (Service) beanClass.getAnnotation(Service.class);
            Class[] interfaces = beanClass.getInterfaces();

            if(!StringUtil.isEmpty(service.name())){
                int i = 0,len=interfaces.length;
                while (i<len) {
                    beanDefinitionMap.put(service.name(),definition);
                    i++;
                }
            }
            else {
                for(Class inf :interfaces){
                    beanDefinitionMap.put(StringUtil.lowerFirstLetter(inf.getSimpleName()),definition);
                }
            }
        }
    }


    /**
     * 获取框架中进行类注入的注解
     * @return
     */
    public Collection<Class<? extends Annotation>> clzAnnotations(){
        LinkedList collection = new LinkedList<Class<? extends Annotation>>();
        collection.add(Controller.class);
        collection.add(Service.class);
        collection.add(Repository.class);
        return collection;
    }

}
