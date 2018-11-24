package com.demo.abel.framework.mvc;

import com.demo.abel.framework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 封装参数位置
 * @author: liuzijian
 * @date: 2018-11-22 11:05
 */
public class HandlerAdapter {

    private final Map<String,Integer> paramPositionMap = new HashMap<>();

    public void setPosition(String paramName,int location){
        paramPositionMap.put(paramName,location);
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response,HandlerMapping handlerMapping){
        Method method = handlerMapping.getMethod();
        Object[] params = null;
        if(!CollectionUtils.isEmpty(paramPositionMap)){
            Map<String,String[]> paramMap = request.getParameterMap();

            params = new Object[paramPositionMap.size()];
            for(Map.Entry<String,String[]> entry:paramMap.entrySet()){
                int position = paramPositionMap.get(entry.getKey());
                Class pmType = method.getParameters()[position].getType();
                Object param;
                if(pmType==Long.class){
                    param = Long.valueOf(entry.getValue()[0]);
                }else {
                    param = entry.getValue();
                }
                params[position] = param;
            }

            Integer index;
            if((index=paramPositionMap.get(HttpServletRequest.class.getSimpleName()))!=null){
                params[index] = request;
            }

            if((index=paramPositionMap.get(HttpServletResponse.class.getSimpleName()))!=null){
                params[index] = response;
            }
        }
        try {
            Object res = method.invoke(handlerMapping.getController(),params);
            if(method.getReturnType() == ModelAndView.class){
                return ModelAndView.class.cast(res);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
