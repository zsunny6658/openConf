package com.sunny.use;

import com.sunny.conf.ConfPath;
import com.sunny.conf.LocalPropertiesUtils;
import com.sunny.conf.PackageUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * create by zsunny
 * data: 2018/8/11
 **/
public class ConfValueDeal {

    public static void putAllConf(){

        Set<Class<?>> classSet = PackageUtil.getClasses("");

        classSet.forEach(ConfValueDeal::putInConf);

    }

    private static void putInConf(Class<?> clazz){

        Field[] fields = clazz.getDeclaredFields();

        Object oo = null;
        try {
            oo = LocalPropertiesUtils.loadProperties("application.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Field field : fields) {

            if(field.isAnnotationPresent(ConfPath.class)){

                //static检查
                if((field.getModifiers()&8) == 0){
                    throw new RuntimeException("配置项必须为static变量");
                }

                ConfPath confPath = field.getAnnotation(ConfPath.class);

                Object o = oo;
                String[] props = confPath.value().split("\\.");

                int ind = 0;
                while (true){

                    if(ind < props.length && o instanceof Map){

                        o = ((Map) o).get(props[ind]);

                    }else {

                        break;
                    }
                    ind ++;
                }

                try {
                    field.setAccessible(true);
                    field.set(field,String.valueOf(o));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }

        }

    }

}
