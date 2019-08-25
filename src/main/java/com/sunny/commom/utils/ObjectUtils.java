package com.sunny.commom.utils;

import com.sunny.source.bean.Node;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author zsunny
 * @Date 2019/4/19 22:10
 * @Mail zsunny@yeah.net
 */
public class ObjectUtils {

    //deep clone except classes not implement clonable
    public static Object deepClone(Object o) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(o);
        // read from the stream
        ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
        ObjectInputStream oi = new ObjectInputStream(bi);
        return (oi.readObject());
    }

    //deep copy for multi-level map
    public static Object deepCopy(Object o) {

        if (o instanceof String
                || o instanceof Integer
                || o instanceof Float
                || o instanceof Double
                || o instanceof Boolean) {
            return o;
        }

        Map<String, Object> ret = new HashMap<>();
        LinkedBlockingQueue<Node> queue = new LinkedBlockingQueue<>();
        queue.offer(new Node((Map<String, Object>) o, ret));

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            Map<String, Object> nodeRes = node.getRes();
            Map<String, Object> nodeSource = node.getSource();
            nodeRes.forEach((key, value) -> {
                if (value instanceof String
                        || value instanceof Integer
                        || value instanceof Float
                        || value instanceof Double
                        || value instanceof Boolean) {
                    nodeSource.put(key, value);
                } else {
                    Map<String, Object> tmp = new HashMap<>();
                    nodeSource.put(key, tmp);
                    queue.offer(new Node((Map<String, Object>) nodeRes.get(key), tmp));
                }
            });
        }

        return ret;
    }

}
