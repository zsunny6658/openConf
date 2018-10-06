package com.sunny.source;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

//todo merge几种情况的配置项目，注意null值处理
public class LoadResult {

    private static LoadFileName[] loadFileNames = {LoadFileName.APPLICATION_YML,
            LoadFileName.APPLICATION_YAML, LoadFileName.APPLICATION_PROPERTIES};

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

            merge(res, (HashMap<String, Object>) sourceResult);

        }

        return res;

    }

    private static void merge(HashMap<String, Object> res, HashMap<String, Object> source){

        LinkedBlockingQueue<Node> queue = new LinkedBlockingQueue<>();
        queue.offer(new Node(res, source));

        while (!queue.isEmpty()){

            Node node = queue.poll();
            HashMap<String, Object> nodeRes = node.getRes();
            HashMap<String, Object> nodeSource = node.getSource();
            nodeSource.forEach((key, value) -> {
                if(nodeRes.containsKey(key)) {
                    if (value instanceof String) {
                        nodeRes.putIfAbsent(key, value);
                    } else {
                        if(!(nodeRes.get(key) instanceof String)){
                            queue.offer(new Node((HashMap<String, Object>) nodeRes.get(key),
                                    (HashMap<String, Object>) nodeSource.get(key)));
                        }
                    }
                }else{
                    nodeRes.put(key, value);
                }
            });

        }

    }

}

class Node{
    private HashMap<String, Object> res;
    private HashMap<String, Object> source;

    public Node(HashMap<String, Object> res, HashMap<String, Object> source) {
        this.res = res;
        this.source = source;
    }

    public HashMap<String, Object> getRes() {
        return res;
    }

    public void setRes(HashMap<String, Object> res) {
        this.res = res;
    }

    public HashMap<String, Object> getSource() {
        return source;
    }

    public void setSource(HashMap<String, Object> source) {
        this.source = source;
    }
}
