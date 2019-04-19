package com.sunny.source;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class Node{
    private Map<String, Object> res;
    private Map<String, Object> source;

    public Node(Map<String, Object> res, Map<String, Object> source) {
        this.res = res;
        this.source = source;
    }

    public Map<String, Object> getRes() {
        return res;
    }

    public void setRes(Map<String, Object> res) {
        this.res = res;
    }

    public Map<String, Object> getSource() {
        return source;
    }

    public void setSource(Map<String, Object> source) {
        this.source = source;
    }


    @SuppressWarnings("unchecked")
	public static void merge(Map<String, Object> res, Map<String, Object> source, boolean isCover){
        LinkedBlockingQueue<Node> queue = new LinkedBlockingQueue<>();
        queue.offer(new Node(res, source));

        while (!queue.isEmpty()){
            Node node = queue.poll();
            Map<String, Object> nodeRes = node.getRes();
            Map<String, Object> nodeSource = node.getSource();
            nodeSource.forEach((key, value) -> {
                if(nodeRes.containsKey(key)) {
                    if (value instanceof String) {
                        //需要判断是否覆盖的情况
                        if(!isCover) {
                            //donnot cover
                            nodeRes.putIfAbsent(key, value);
                        }else {
                            //cover
                            nodeRes.put(key, value);
                        }
                    } else {
                        if(!(nodeRes.get(key) instanceof String)){
                            queue.offer(new Node((Map<String, Object>) nodeRes.get(key),
                                    (Map<String, Object>) nodeSource.get(key)));
                        }
                    }
                }else{
                    nodeRes.put(key, value);
                }
            });

        }

    }
}
