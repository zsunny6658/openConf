package com.sunny.source;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Node{
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


    public static void merge(HashMap<String, Object> res, HashMap<String, Object> source, boolean isActive){

        LinkedBlockingQueue<Node> queue = new LinkedBlockingQueue<>();
        queue.offer(new Node(res, source));

        while (!queue.isEmpty()){

            Node node = queue.poll();
            HashMap<String, Object> nodeRes = node.getRes();
            HashMap<String, Object> nodeSource = node.getSource();
            nodeSource.forEach((key, value) -> {
                if(nodeRes.containsKey(key)) {
                    if (value instanceof String) {
                        //需要判断是否覆盖的情况
                        if(!isActive) {
                            nodeRes.putIfAbsent(key, value);
                        }else {
                            nodeRes.put(key, value);
                        }
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
