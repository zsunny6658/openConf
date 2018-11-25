package com.sunny.processor;

import com.sunny.annotation.ConfClass;
import com.sunny.annotation.ConfClassDefault;
import com.sunny.annotation.ConfClassIgnore;
import com.sunny.annotation.ConfClassPrefix;
import com.sunny.source.LoadResult;
import com.sunny.source.filter.ConfFilter;
import com.sunny.utils.PackageUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

public class ConfClassProcessor extends ConfProcessor{

    public static void process(){
        Set<Class<?>> classSet = PackageUtil.getAllClassSet();

        Object oo = LoadResult.getSource();

        classSet.forEach(clazz -> putInConf(oo, clazz));
    }

    public static void putInConf(Object oo, Class<?> clazz){

        String prefix = "";

        if(!clazz.isAnnotationPresent(ConfClass.class)){
            return;
        }

        if(clazz.isAnnotationPresent(ConfClassPrefix.class)){
            ConfClassPrefix confClassPrefix = clazz.getAnnotation(ConfClassPrefix.class);
            prefix = confClassPrefix.value();
        }

        if(prefix.endsWith(".")){
            prefix = prefix.substring(0, prefix.length()-1);
        }

        Field[] fields = clazz.getDeclaredFields();

        for(Field field : fields){

            //ignore或者非静态
            if(field.isAnnotationPresent(ConfClassIgnore.class) ||
                    (field.getModifiers()&8) == 0 ){
                continue;
            }

            boolean isDefault = false;
            if(field.isAnnotationPresent(ConfClassDefault.class)){
                ConfClassDefault confClassDefault = field.getAnnotation(ConfClassDefault.class);
                String v = confClassDefault.value();
                try {
                    field.setAccessible(true);
                    field.set(field, v);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                isDefault = true;
            }

            String tmpPrefix = prefix + "." + field.getName();
            String[] props = tmpPrefix.split("\\.");

            putInConfCore(oo, props, field, isDefault);
        }
    }

    public static void putInConfCore(Object o, String[] props, Field field, boolean isDefault){

        int ind = 0;
        while (true){

            if(ind < props.length && null != o && o instanceof Map){

                o = ((Map) o).get(props[ind]);

            }else {

                break;
            }
            ind ++;
        }

        if(null == o && isDefault){
            return;
        }

        try {
            field.setAccessible(true);
            field.set(field,String.valueOf(o));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
