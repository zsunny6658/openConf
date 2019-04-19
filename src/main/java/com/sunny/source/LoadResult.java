package com.sunny.source;

import java.util.*;
import java.util.concurrent.Executor;

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
		source = getSources(false);
	}

	public static void updateResult() throws Exception{
		source = getSources(true);
	}

	private static void loadOtherConfSource() {
		Set<Class<?>> classSet = PackageUtil.getAllClassSet();
		classSet.forEach(LoadResult::loadConfSource);
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
	private static Object getSources(boolean isUpdate) throws Exception {
		if(!isUpdate) {
			Collections.sort(loadFileNameList, (o1, o2) -> {
				if (o1.getOrder() == o2.getOrder()) {
					return o1.getFileName().compareTo(o2.getFileName());
				}
				return o2.getOrder() - o1.getOrder();
			});
		}else{
			Collections.sort(loadFileNameList, (o1, o2) -> {
				if (o1.getOrder() == o2.getOrder()) {
					return o2.getFileName().compareTo(o1.getFileName());
				}
				return o1.getOrder() - o2.getOrder();
			});
		}
		Map<String, Object> res = new HashMap<>();
//		System.out.println(loadFileNameList);
		for (LoadFileName loadFileName : loadFileNameList) {
			Object sourceResult = loadFileName.getLoadSource().loadSources(loadFileName.getFileName());
			if (null == sourceResult) {
				continue;
			}
			if (0 == res.size()) {
				res = (Map<String, Object>) sourceResult;
				continue;
			}
			if(!isUpdate)
				Node.merge(res, (Map<String, Object>) sourceResult, false);
			else
				Node.merge(res, (Map<String, Object>) sourceResult, true);
		}
		ConfFilter.filter(res, isUpdate);
		return res;
	}

}
