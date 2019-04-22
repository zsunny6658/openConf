package com.sunny.processor;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.sunny.annotation.*;
import com.sunny.source.LoadResult;
import com.sunny.utils.PackageUtil;

public class ConfClassProcessor extends AbstractConfProcessor {

	@Override
	public void update() {
		if(dynamicClassSet.size() > 0){
			//create a new thread
			if(dynamicFieldSet.size() > 0){
				//create a new thread
				tp.scheduleAtFixedRate(new Thread(()->{
					try {
						updateConfSource();
					} catch (Exception e) {
						e.printStackTrace();
					}
					dynamicClassSet.forEach(clazz -> putInConf(oo, clazz));
				}),interval,interval, unit);
			}
		}
	}

	@Override
	public void process() {
		classSet.forEach(clazz -> putInConf(oo, clazz));
		update();
	}

	public static void putInConf(Object oo, Class<?> clazz) {
		String prefix = "";
		if (!clazz.isAnnotationPresent(ConfClass.class)) {
			return;
		}
		if (clazz.isAnnotationPresent(ConfClassPrefix.class)) {
			ConfClassPrefix confClassPrefix = clazz.getAnnotation(ConfClassPrefix.class);
			prefix = confClassPrefix.value();
		}
		if (prefix.endsWith(".")) {
			prefix = prefix.substring(0, prefix.length() - 1);
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			// ignored or not static
			if (field.isAnnotationPresent(ConfClassIgnore.class) || (field.getModifiers() & 8) == 0) {
				continue;
			}
			//about default value
			boolean isDefault = false;
			if (field.isAnnotationPresent(ConfClassDefault.class)) {
				ConfClassDefault confClassDefault = field.getAnnotation(ConfClassDefault.class);
				String v = confClassDefault.value();
				try {
					field.setAccessible(true);
					field.set(field, v);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				isDefault = true;
			}
			//about alias
			String alias = null;
			if(field.isAnnotationPresent(ConfClassAlias.class)){
				ConfClassAlias confClassAlias = field.getAnnotation(ConfClassAlias.class);
				alias = confClassAlias.value();
			}
			//produce the prefix
			String tmpPrefix;
			if(null == alias)
				tmpPrefix = prefix + "." + field.getName();
			else
				tmpPrefix = prefix + "." + alias;
			String[] props = tmpPrefix.split("\\.");

			putInConfCore(oo, props, field, isDefault);
		}
	}

	public static void putInConfCore(Object o, String[] props, Field field, boolean isDefault) {
		//speed up
		if(isDefault)
			return;

		int ind = 0;
		while (true) {
			if (ind < props.length && null != o && o instanceof Map) {
				o = ((Map<?, ?>) o).get(props[ind]);
			} else {
				break;
			}
			ind++;
		}

		if (null == o) {
			return;
		}

		try {
			field.setAccessible(true);
			field.set(field, String.valueOf(o));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
