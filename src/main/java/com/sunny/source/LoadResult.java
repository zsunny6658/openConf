package com.sunny.source;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.lang.model.type.UnknownTypeException;

import com.sunny.annotation.ConfSource;
import com.sunny.source.bean.Content;
import com.sunny.source.bean.LoadFileName;
import com.sunny.source.bean.Node;
import com.sunny.source.file.LoadProperties;
import com.sunny.source.file.LoadXml;
import com.sunny.source.file.LoadYaml;
import com.sunny.source.filter.ConfFilter;
import com.sunny.utils.FileUtil;
import com.sunny.utils.PackageUtil;

public class LoadResult {

	private static List<LoadFileName> loadFileNameList = new ArrayList<>(Arrays.asList(LoadFileName.APPLICATION_YML, LoadFileName.APPLICATION_YAML,
			LoadFileName.APPLICATION_PROPERTIES, LoadFileName.APPLICATION_XML));
	private static Object source = null;
	private static Map<LoadFileName, Content> resMap = new TreeMap<>();

	public static void add(LoadFileName loadFile){
		loadFileNameList.add(loadFile);
	}
	public static void remove(LoadFileName loadFile){
		loadFileNameList.remove(loadFile);
	}

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
		Collections.sort(loadFileNameList);
		Map<String, Object> res = new HashMap<>();
		for (LoadFileName loadFileName : loadFileNameList) {
			Object sourceResult = null;
			boolean needUpdate = false;
			if(!isUpdate) {
				sourceResult = loadFileName.getLoadSource().loadSources(loadFileName.getFileName());
				needUpdate = true;
			}else{
				long recModifyTime = 0;
				if(null != resMap.get(loadFileName)){
					recModifyTime = resMap.get(loadFileName).getModifyTime();
				}
				File file = FileUtil.getFile(loadFileName.getFileName());
				long modifyTime = file.lastModified();
				needUpdate = (modifyTime > recModifyTime);
				if(needUpdate) {
					sourceResult = loadFileName.getLoadSource().loadSources(loadFileName.getFileName());
					resMap.get(loadFileName).setModifyTime(modifyTime);
				}else{
					if(null == resMap.get(loadFileName))
						sourceResult = null;
					else
						sourceResult = resMap.get(loadFileName).getContent();
				}
			}
			if (null == sourceResult) {
				continue;
			}
			if(needUpdate)
				resMap.put(loadFileName, new Content(sourceResult));
			if (0 == res.size()) {
				res = (Map<String, Object>) sourceResult;
				continue;
			}
			Node.merge(res, (Map<String, Object>) sourceResult, false);
		}
		ConfFilter.filter(res, isUpdate);
		return res;
	}
}
