package com.sunny.source.file;

import com.sunny.source.LoadSource;
import com.sunny.utils.FileUtil;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * created by zsunny
 * Email zsunny@yeah.net
 * Date on 2018/10/5.
 */
/**
 * 枚举类型实现单例，也可以考虑通过静态内部类等其他放松
 */
public enum LoadProperties implements LoadSource{

    loadProperties;

    @Override
    public Object loadSources(String path) throws Exception {

        Properties properties = new Properties();

        InputStream in = FileUtil.getFileInputStream(path);

        if(null == in)
            return null;

        properties.load(in);

        return convertToMap(properties);
    }

    public HashMap<String, Object> convertToMap(Properties properties){

        if(!properties.keys().hasMoreElements()){
            return null;
        }

        HashMap<String, Object> res = new HashMap<>();

        //处理字符串类型的属性
        for (Enumeration<?> e = properties.keys(); e.hasMoreElements() ;) {
            Object k = e.nextElement();
            Object v = properties.get(k);
            if (k instanceof String && v instanceof String) {

                String[] ks = ((String) k).split("\\.");
                HashMap<String, Object> cres = res;
                String ck = "";
                for(int i=0; i<ks.length; i++){
                    ck = ks[i];
                    if(i == ks.length - 1)
                        break;
                    HashMap<String, Object> nextMap = new HashMap<>();
                    cres.putIfAbsent(ck, nextMap);
                    cres = (HashMap<String, Object>) cres.get(ck);
                }

                cres.put(ck, (String) v);
            }
        }



        return res;

    }

}
