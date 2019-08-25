package com.sunny.commom.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {

    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 根目录为classpath
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static String readFile(String path) throws IOException {
        InputStream ins = getFileInputStream(path);
        if (Objects.isNull(ins))
            return null;
        InputStreamReader in = new InputStreamReader(ins);
        StringBuilder res = new StringBuilder();

        int c;
        while ((c = in.read()) != -1) {
            res.append((char) c);
        }
        in.close();

        return res.toString();
    }


    /**
     * 获取指定文件（classpath下的）的inputStream
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static InputStream getFileInputStream(String path) throws IOException {
        if (!judgeFileExist(path))
            return null;
        File file = getFile(path);
        return new FileInputStream(file);
    }

    /**
     * 根目录为classpath。
     *
     * @param path
     * @param content
     * @throws IOException
     */
    public static void writeFile(String path, String content) throws IOException {
        File file = getFile(path);
        if (!judgeFileExist(path)) {
            file.createNewFile();
        }
        FileWriter out = new FileWriter(file);
        out.write(content);
        out.flush();
        out.close();
    }

    public static boolean judgeFileExist(String path) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        File file = new File(url.getPath() + path);
        return Objects.nonNull(file.getParentFile()) && file.getParentFile().exists() && file.exists();
    }

    /**
     * 判断路径父文件夹是否存在，不存在则创建
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static File getFile(String path) throws IOException {
        //URL url = FileUtils.class.getClassLoader().getResource("");
        //更通用方案
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        File file = new File(url.getPath(), path);
        if (!file.getParentFile().exists()) {
            boolean mkResult = file.getParentFile().mkdirs();
            if (!mkResult) {
                log.error("创建文件夹失败");
            }
        }
        return file;
    }

}
