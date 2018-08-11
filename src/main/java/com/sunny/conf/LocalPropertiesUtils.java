package com.sunny.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;

/**
 * created by zsunny
 * Email zsunny@yeah.net
 * Date on 2018/7/30.
 */
//TODO 考虑之后同时获取yml和properties文件并且将属性按策略合并
public class LocalPropertiesUtils {

    private static final Logger log = LoggerFactory.getLogger(LocalPropertiesUtils.class);

    public static Object loadProperties(String path) throws IOException {

        Yaml yaml = new Yaml();

        Object o =  yaml.load(readFile(path));

        return o;

    }

    /**
     * 根目录为classpath
     * @param path
     * @return
     * @throws IOException
     */
    private static String readFile(String path) throws IOException {

        File file = getFile(path);

        if(!judgeFileExist(file))
            file.createNewFile();

        InputStreamReader in = new InputStreamReader(new FileInputStream(file));

        StringBuilder res = new StringBuilder();

        int c;

        while ( (c = in.read()) != -1){
            res.append((char)c);
        }

        in.close();

        return res.toString();

    }

    /**
     * 根目录为classpath。
     * @param path
     * @param content
     * @throws IOException
     */
    private static void writeFile(String path,String content) throws IOException {

        File file = getFile(path);

        if(!judgeFileExist(file)){

            file.createNewFile();

        }

        FileWriter out = new FileWriter(file);

        out.write(content);

        out.flush();

        out.close();

    }

    private static boolean judgeFileExist(File file){

        return file.exists();

    }

    private static File getFile(String path) throws IOException {

        //URL url = FileUtils.class.getClassLoader().getResource("");
        //更通用方案
        URL url = Thread.currentThread().getContextClassLoader().getResource("");

        File file = new File(url.getPath()+path);

        if(!file.getParentFile().exists()){

            boolean mkResult = file.getParentFile().mkdirs();
            if (!mkResult) {
                System.out.println("创建文件夹失败");
            }

        }

        return file;

    }

}
