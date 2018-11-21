package com.demo.abel.framework.context;

import com.demo.abel.framework.constant.AbelConstants;

import javax.servlet.ServletConfig;
import java.io.IOException;
import java.io.InputStream;

/**
 * @description: TODO 类描述
 * @author: liuzijian
 * @create: 2018-11-13 23:40
 **/
public class AnnotationApplicationContext extends AbstractApplicationContext{

    public AnnotationApplicationContext(ServletConfig servletConfig) {
        //定位
        String propertyPath = servletConfig.getInitParameter(AbelConstants.PROPERTY_PATH);
        InputStream is = getClass().getClassLoader().getResourceAsStream(propertyPath.replace(AbelConstants.IGNORE_PREFIX,""));
        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        refresh();
    }
}
