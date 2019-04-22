package com.sunny.processor;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import com.sunny.annotation.ConfPath;
import com.sunny.annotation.SystemConfPath;
import com.sunny.source.filter.ConfFilter;

/**
 * create by zsunny
 * data: 2018/8/11
 **/
public class ConfValueProcessor extends AbstractConfProcessor{

    @Override
    public void update() {
        //dynamic update
        if(dynamicFieldSet.size() > 0){
            //create a new thread
            tp.scheduleAtFixedRate(new Thread(()->{
                try {
                    updateConfSource();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dynamicFieldSet.forEach(filed -> putInConfCore(oo, filed, false));
            }),interval,interval, unit);
        }
    }

    @Override
    public void process(){
        classSet.forEach(clazz -> putInConf(oo, clazz));
        update();
    }



    /**
     * 处理配置入属性
     * @param oo
     * @param clazz
     */
    private static void putInConf(Object oo, Class<?> clazz){
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if(field.isAnnotationPresent(ConfPath.class)){
                //static检查
                if((field.getModifiers()&8) == 0){
                    throw new RuntimeException("配置项必须为static变量");
                }
                Object o = oo;
                putInConfCore(o, field, false);
            }else if(field.isAnnotationPresent(SystemConfPath.class)){
                //static检查
                if((field.getModifiers()&8) == 0){
                    throw new RuntimeException("配置项必须为static变量");
                }
                putInConfCore(ConfFilter.getSystemMap(), field, true);
            }
        }
    }

    /**
     * 配置入属性核心处理
     * @param o Object
     * @param field Field
     * @Param system boolean
     */
    private static void putInConfCore(Object o, Field field, boolean system){
        //get props
        String[] props;
        if(system){
            SystemConfPath systemConfPath = field.getAnnotation(SystemConfPath.class);
            props = systemConfPath.value().split("\\.");
        }else{
            ConfPath confPath = field.getAnnotation(ConfPath.class);
            props = confPath.value().split("\\.");
        }
        //process
        int ind = 0;
        while (true){
            if(ind < props.length && null != o && o instanceof Map){
                o = ((Map<?, ?>) o).get(props[ind]);
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
