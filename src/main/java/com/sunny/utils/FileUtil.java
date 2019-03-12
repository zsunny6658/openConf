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
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String path) throws IOException {
		InputStream ins = getFileInputStream(path);
		if (null == ins)
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
		if (path == null || path.length() == 0 || path.trim().length() == 0) {
			return null;
		}
		path = path.trim();
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
	 *             io异常
	 * @throws NullPointerException
	 *             文件不存在空指针异常
	 */
	public static void writeFile(String path, String content) throws IOException {
		File file = getFile(path);
		if (file == null) {
			throw new NullPointerException();
		}
		if (!judgeFileExist(path)) {
			file.createNewFile();
		}
		FileWriter out = new FileWriter(file);
		out.write(content);
		out.flush();
		out.close();
	}

	/**
	 * 判断路径是否存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean judgeFileExist(String path) {
		URL url = null;
		File file = null;
		// url = Thread.currentThread().getContextClassLoader().getResource("");
		url = Thread.currentThread().getContextClassLoader().getResource(path);
		if (url == null) {
			return false;
		}
		file = new File(url.getPath());
		if (null != file.getParentFile() && file.getParentFile().exists())
			return file.exists();
		return false;
	}

	/**
	 * 判断路径父文件夹是否存在，不存在则创建
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private static File getFile(String path) throws IOException {
		URL url = null;
		File file = null;
		// url = Thread.currentThread().getContextClassLoader().getResource("");
		url = Thread.currentThread().getContextClassLoader().getResource(path);
		if (url == null) {
			return null;
		}
		file = new File(url.getPath());
		if (!file.getParentFile().exists()) {
			boolean mkResult = file.getParentFile().mkdirs();
			if (!mkResult) {
				log.error("创建文件夹失败");
			}
		}
		return file;
	}

}
