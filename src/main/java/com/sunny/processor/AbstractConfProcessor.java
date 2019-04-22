package com.sunny.processor;

import com.sunny.annotation.*;
import com.sunny.source.LoadResult;
import com.sunny.source.filter.ConfFilter;
import com.sunny.utils.PackageUtil;

import javax.management.relation.RoleUnresolved;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class AbstractConfProcessor {

    protected static final ScheduledExecutorService tp = Executors.newScheduledThreadPool(2);

    protected static Set<Class<?>> classSet;
    protected static Object oo;
    //dynamic update sets
    protected static Set<Class<?>> dynamicClassSet = new HashSet<>();
    protected static Set<Field> dynamicFieldSet = new HashSet<>();
    protected static int interval = 10;
    protected static TimeUnit unit = TimeUnit.SECONDS;

    static {
        init();
    }

    protected static void init() {
        classSet = PackageUtil.getAllClassSet();
        oo = LoadResult.getSource();
        getDynamics();
        getInterval();
    }
    public static void updateConfSource() throws Exception {
        LoadResult.updateResult();
        oo = LoadResult.getSource();
    }
    //get dynamic interval
    private static void getInterval(){
        Object intv = ConfFilter.getSystemConf(ConfFilter.DYNAMIC_INTERVAL);
        Object u = ConfFilter.getSystemConf(ConfFilter.DYNAMIC_UNIT);
        if(null != intv){
            try {
                interval = (int)intv;
            }catch (RuntimeException e){
                e.printStackTrace();
            }
        }
        if(null != u){
            String su = null;
            try {
                su = (String)u;
            }catch (RuntimeException e){
                e.printStackTrace();
            }
            switch (su){
                case "h":
                    unit = TimeUnit.HOURS;
                    break;
                case "m":
                    unit = TimeUnit.MINUTES;
                    break;
                default:
                    unit = TimeUnit.SECONDS;
            }
        }
    }
    //get dynamic classes and fileds
    private static void getDynamics(){
        classSet.forEach(clazz->{
            if(clazz.isAnnotationPresent(Dynamic.class)){
                dynamicClassSet.add(clazz);
            }else{
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    //don't support for systemconf at present. look forward to future version
                    if((field.isAnnotationPresent(ConfPath.class) || (clazz.isAnnotationPresent(ConfClass.class) && !field.isAnnotationPresent(ConfClassIgnore.class)))
                            &&  (field.getModifiers()&8) != 0
                            && field.isAnnotationPresent(Dynamic.class)){
                        dynamicFieldSet.add(field);
                    }
                }
            }
        });
    }
    public static void stopThreadPool(){
        tp.shutdown();
    }
    public abstract void update();
    public abstract void process();
}
