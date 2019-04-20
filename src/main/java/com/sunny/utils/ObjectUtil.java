package com.sunny.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author zsunny
 * @Date 2019/4/19 22:10
 * @Mail zsunny@yeah.net
 */
public class ObjectUtil {

    public static Object deepClone(Object o) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(o);
        // read from the stream
        ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
        ObjectInputStream oi = new ObjectInputStream(bi);
        return (oi.readObject());
    }

    public static void deepCopy(Object o){

    }

}
