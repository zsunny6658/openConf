package com.sunny.processor;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.sunny.annotation.ConfPath;
import com.sunny.annotation.SystemConfPath;
import com.sunny.source.filter.ConfFilter;

/**
 * create by zsunny
 * data: 2018/8/11
 **/
public class ConfValueProcessor extends AbstractConfProcessor {

    @Override
    public void update() {
        //dynamic update
        if(dynamicFieldSet.size() > 0){
            try {
                getEvent();
            } catch (IOException e) {
                e.printStackTrace();
            }


            //create a new thread
            //没有变化就不用执行putInConfCore
//            tp.scheduleAtFixedRate(new Thread(()->{
//                try {
//                    updateConfSource();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                dynamicFieldSet.forEach(filed -> putInConfCore(oo, filed, false));
//            }),interval,interval, TimeUnit.SECONDS);
        }
    }

    @Override
    public void process(){
        classSet.forEach(clazz -> putInConf(oo, clazz));
        update();
    }


    private void getEvent() throws IOException{
        String dirs = System.getProperty("user.dir")+"\\src\\main\\resources";
        Path path = Paths.get(dirs);
        WatchService watcher = FileSystems.getDefault().newWatchService();
        path.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
        new Thread(() -> {
            try {
                boolean flag = true;
                while (true) {
                    System.out.println("hj");
                    WatchKey key = watcher.take();
                    int i = 0;
                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
                            continue;
                        }
                        if (flag){
                            flag = false;
                            continue;
                        }
                        try {
                            updateConfSource();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dynamicFieldSet.forEach(filed -> putInConfCore(oo, filed, false));
                        flag = false;
                    }
                    if (!key.reset()) { // 重设WatchKey
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
//
//
//        try {
//            Thread.sleep(2000 * 60 * 10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
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
