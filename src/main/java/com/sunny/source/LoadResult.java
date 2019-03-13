package com.sunny.source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.type.UnknownTypeException;

import com.sunny.annotation.ConfSource;
import com.sunny.source.file.LoadProperties;
import com.sunny.source.file.LoadXml;
import com.sunny.source.file.LoadYaml;
import com.sunny.source.filter.ConfFilter;
import com.sunny.utils.PackageUtil;

public class LoadResult {

	private static List<LoadFileName> loadFileNameList = new ArrayList<>(Arrays.asList(LoadFileName.APPLICATION_YML, LoadFileName.APPLICATION_YAML,
			LoadFileName.APPLICATION_PROPERTIES, LoadFileName.APPLICATION_XML));
	
	public static void add(LoadFileName loadFile){
		loadFileNameList.add(loadFile);
	}
	
	public static void remove(LoadFileName loadFile){
		loadFileNameList.remove(loadFile);
	}
	
	private static Object source = null;

	public static void loadResult() throws Exception {
		//前置处理注解@ConfSource,用于获取默认配置之外的配置文件
		loadOtherConfSource();
		source = getSources();
	}

	private static void loadOtherConfSource() {
		Set<Class<?>> classSet = PackageUtil.getAllClassSet();
		classSet.forEach(clazz -> loadConfSource(clazz));
	}

	//加载自定义的配置文件
	private static void loadConfSource(Class<?> clazz) {
		if (!clazz.isAnnotationPresent(ConfSource.class)) {
			return;
		}
		if (clazz.isAnnotationPresent(ConfSource.class)) {
			ConfSource confSource = clazz.getAnnotation(ConfSource.class);
			String fileName = confSource.value();//配置文件名/路径
			LoadSource loadSource = getLoadSource(fileName);
			LoadFileName loadFile = new LoadFileName(fileName, loadSource);
			loadFileNameList.add(loadFile);
		}
	}

	private static LoadSource getLoadSource(String fileName) {
		if(fileName.endsWith(".yaml")){
			return LoadYaml.getInstance();
		}else if(fileName.endsWith(".yml")){
			return LoadYaml.getInstance();
		}else if(fileName.endsWith(".properties")){
			return LoadProperties.getInstance();
		}else if(fileName.endsWith(".xml")){
			return LoadXml.getInstance();
		}else{
			throw new UnknownTypeException(null, "暂时不支持此种配置文件");
		}
	}

	public static Object getSource() {
		return source;
	}

	@SuppressWarnings("unchecked")
	public static Object getSources() throws Exception {
		// Arrays.sort(loadFileNames);
		Collections.sort(loadFileNameList);
		Map<String, Object> res = new HashMap<>();
		for (LoadFileName loadFileName : loadFileNameList) {
			Object sourceResult = loadFileName.getLoadSource().loadSources(loadFileName.getFileName());
			if (null == sourceResult) {
				continue;
			}
			if (0 == res.size()) {
				res = (Map<String, Object>) sourceResult;
				continue;
			}
			Node.merge(res, (Map<String, Object>) sourceResult, false);
		}
		ConfFilter.filter(res);
		return res;
	}

}
