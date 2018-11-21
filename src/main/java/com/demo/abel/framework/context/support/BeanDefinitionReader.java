package com.demo.abel.framework.context.support;

import com.demo.abel.framework.context.bean.BeanDefinition;
import com.demo.abel.framework.constant.AbelConstants;
import com.demo.abel.framework.context.AbstractApplicationContext;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

/**
 * @description: `加载`环节的实际处理类
 * @author: liuzijian
 * @create: 2018-11-15 10:08
 **/
public class BeanDefinitionReader {

    private AbstractApplicationContext context;

    public BeanDefinitionReader(AbstractApplicationContext context) {
        this.context = context;
    }

    public void loadBeanDefinitions(String rootPath){
        doScan(rootPath);
    }

    private void doScan(String rootPath){
        String filePath = rootPath.replace(".",File.separator);
        URI uri = null;
        try {
            uri = getClass().getClassLoader().getResource(filePath).toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File file = new File(uri);

        File[] files =  file.listFiles();
        for(File curFile:files){
            if(curFile.isDirectory()){
                doScan(rootPath+"."+curFile.getName());
            }else {
                String curFileName = curFile.getName().replace(AbelConstants.TYPE_SUFFIX,"");

                try {
                    Class clz = Class.forName(rootPath+"."+curFileName);
                    if(isFrameworkBean(clz)){
                        context.registy(new BeanDefinition(clz));
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 是否被框架管理的bean
     * @param clz
     * @return
     */
    private boolean isFrameworkBean(Class clz){
        Collection<Class<? extends Annotation>> clzAnnotations = context.clzAnnotations();
        Annotation[] annotations = clz.getAnnotations();
        if(annotations==null || annotations.length==0){
            return false;
        }

        for(Annotation atn:annotations){
            if(clzAnnotations.contains(atn.annotationType())){
                return true;
            }
        }
        return false;
    }
}
