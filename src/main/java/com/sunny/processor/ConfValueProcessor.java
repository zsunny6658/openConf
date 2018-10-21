package com.sunny.processor;

import com.sunny.annotation.ConfPath;
import com.sunny.annotation.SystemConfPath;
import com.sunny.source.LoadResult;
import com.sunny.source.file.LoadYaml;
import com.sunny.source.filter.ConfFilter;
import com.sunny.source.listener.ConfListner;
import com.sunny.utils.PackageUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * create by zsunny
 * data: 2018/8/11
 **/
public class ConfValueProcessor {

    public static void putAllConf(){

        Set<Class<?>> classSet = PackageUtil.getClasses("");

        Object oo = getConfObject();

        ConfListner confListner = null;
        try {
            confListner = getListener();
        } catch (Exception e){
            e.printStackTrace();
        }

        if(null != confListner)
            confListner.doBefore();

        classSet.forEach(clazz -> putInConf(oo, clazz));

        if(null != confListner)
            confListner.doAfter();

    }

    private static ConfListner getListener() throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        String listenerClass = "";

        String[] confPath = ConfFilter.CONF_LISTENER.split("\\.");
        HashMap<String, Object> map = ConfFilter.getSystemMap();
        for(int i=0; i < confPath.length; i++){
            if(i < confPath.length-1){
                if(map.get(confPath[i]) instanceof String)
                    return null;
                map = (HashMap<String, Object>) map.get(confPath[i]);
            }else{
                if(!(map.get(confPath[i]) instanceof String))
                    return null;
                listenerClass = (String) map.get(confPath[i]);
            }
        }
        if("".equals(listenerClass))
            return null;
        ConfListner confListner = (ConfListner) Class.forName(listenerClass).newInstance();
        return confListner;
    }

    //获取配置
    private static Object getConfObject(){
        Object oo = null;
        try {
            oo = LoadResult.getSources();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oo;
    }

    private static void putInConf(Object oo, Class<?> clazz){

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {

            Object o = oo;

            if(field.isAnnotationPresent(ConfPath.class)){

                //static检查
                if((field.getModifiers()&8) == 0){
                    throw new RuntimeException("配置项必须为static变量");
                }

                ConfPath confPath = field.getAnnotation(ConfPath.class);
                String[] props = confPath.value().split("\\.");
                putInConfCore(o, props, field);


            }else if(field.isAnnotationPresent(SystemConfPath.class)){

                //static检查
                if((field.getModifiers()&8) == 0){
                    throw new RuntimeException("配置项必须为static变量");
                }

                SystemConfPath systemConfPath = field.getAnnotation(SystemConfPath.class);
                String[] systemProps = systemConfPath.value().split("\\.");
                putInConfCore(ConfFilter.getSystemMap(), systemProps, field);

            }

        }

    }

    private static void putInConfCore(Object o, String[] props, Field field){
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
