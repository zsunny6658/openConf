package com.sunny.source;

//TODO 考虑线程安全
public interface LoadSource {

	/**
	 * 加载配置资源数据
	 * 
	 * @param path
	 *            配置文件地址
	 * @return 配置数据对象
	 * @throws Exception
	 *             异常
	 */
	Object loadSources(String path) throws Exception;
}
