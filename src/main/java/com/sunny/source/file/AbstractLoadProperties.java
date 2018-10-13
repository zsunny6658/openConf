package com.sunny.source.file;

import com.sunny.source.LoadSource;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public abstract class AbstractLoadProperties implements LoadSource {

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
