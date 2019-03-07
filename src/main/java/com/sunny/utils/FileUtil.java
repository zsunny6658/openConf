package com.sunny.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {

    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 根目录为classpath
     * @param path
     * @return
     * @throws IOException
     */
    public static String readFile(String path) throws IOException {
        InputStream ins = getFileInputStream(path);
        if(null == ins)
            return null;

        InputStreamReader in = new InputStreamReader(ins);
        StringBuilder res = new StringBuilder();

        int c;
        while ( (c = in.read()) != -1){
            res.append((char)c);
        }

        in.close();

        return res.toString();

    }


    /**
     * 获取指定文件（classpath下的）的inputStream
     * @param path
     * @return
     * @throws IOException
     */
    public static InputStream getFileInputStream(String path) throws IOException {
    	if (path == null || path.length() == 0 || path.trim().length() == 0) {
			return null;
		}
		path = path.trim();
        if(!judgeFileExist(path))
            return null;
        File file = getFile(path);
        return new FileInputStream(file);
    }

    /**
     * 根目录为classpath。
     * @param path
     * @param content
     * @throws IOException
     */
    public static void writeFile(String path,String content) throws IOException {
        File file = getFile(path);
        if(!judgeFileExist(path)){
            file.createNewFile();
        }
        FileWriter out = new FileWriter(file);
        out.write(content);
        out.flush();
        out.close();
    }

    public static boolean judgeFileExist(String path){
		URL url = null;
		File file = null;
		if (path.startsWith("classpath:")) {
			path = path.replaceFirst("classpath:", "").trim();
			url = Thread.currentThread().getContextClassLoader().getResource(path);
			file = new File(url.getPath());
		}else{
			url = Thread.currentThread().getContextClassLoader().getResource("");
			file = new File(url.getPath() + path);
		}
        if(null != file.getParentFile() && file.getParentFile().exists())
            return file.exists();
        return false;
    }

    /**
     * 判断路径父文件夹是否存在，不存在则创建
     * @param path
     * @return
     * @throws IOException
     */
    private static File getFile(String path) throws IOException {
        //URL url = FileUtils.class.getClassLoader().getResource("");
        //更通用方案
    	URL url = null;
		File file = null;
		if (path.startsWith("classpath:")) {
			path = path.replaceFirst("classpath:", "").trim();
			url = Thread.currentThread().getContextClassLoader().getResource(path);
			file = new File(url.getPath());
		}else{
			url = Thread.currentThread().getContextClassLoader().getResource("");
			file = new File(url.getPath() + path);
		}

        if(!file.getParentFile().exists()){
            boolean mkResult = file.getParentFile().mkdirs();
            if (!mkResult) {
                log.error("创建文件夹失败");
            }
        }

        return file;

    }

}
