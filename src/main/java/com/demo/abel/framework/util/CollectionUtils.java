package com.demo.abel.framework.util;

import java.util.Collection;
import java.util.Map;

/**
 * @description: CollectionUtils
 * @author: liuzijian
 * @create: 2018-11-23 08:47
 **/
public class CollectionUtils {
    public static boolean isEmpty(Collection collection){
        return collection!=null && collection.size()>0;
    }

    public static boolean isEmpty(Map map){
        return map!=null && map.size()>0;
    }
}
