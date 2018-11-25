package com.sunny.source;

import com.sunny.source.filter.ConfFilter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class LoadResult {

    private static LoadFileName[] loadFileNames = {LoadFileName.APPLICATION_YML,
            LoadFileName.APPLICATION_YAML, LoadFileName.APPLICATION_PROPERTIES, LoadFileName.APPLICATION_XML};

    private static Object source = null;

    public static void LoadResult() throws Exception {
        source = getSources();
    }

    public static Object getSource(){
        return source;
    }

    public static Object getSources() throws Exception {

        Arrays.sort(loadFileNames);

        HashMap<String, Object> res = new HashMap<>();

        for(LoadFileName loadFileName: loadFileNames){

            Object sourceResult = loadFileName.getLoadSource().loadSources(loadFileName.getFileName());

            if(null == sourceResult){
                continue;
            }
            if(0 == res.size()){
                res = (HashMap<String, Object>) sourceResult;
                continue;
            }

            Node.merge(res, (HashMap<String, Object>) sourceResult,false);

        }

        ConfFilter.filter(res);

        return res;

    }


}

