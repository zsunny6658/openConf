package com.sunny.processor;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import com.sunny.annotation.ConfPath;
import com.sunny.annotation.SystemConfPath;
import com.sunny.source.LoadResult;
import com.sunny.source.filter.ConfFilter;
import com.sunny.utils.PackageUtil;

/**
 * create by zsunny data: 2018/8/11
 **/
public class ConfValueProcessor extends ConfProcessor {

	@Override
	public void process() {
		// 获取类
		Set<Class<?>> classSet = PackageUtil.getAllClassSet();
		// 获取配置
		Object oo = LoadResult.getSource();
		// 执行操作
		classSet.forEach(clazz -> putInConf(oo, clazz));
	}

	/**
	 * 处理配置入属性
	 * 
	 * @param oo
	 * @param clazz
	 */
	private static void putInConf(Object oo, Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			Object o = oo;
			if (field.isAnnotationPresent(ConfPath.class)) {
				// static检查
				if ((field.getModifiers() & 8) == 0) {
					throw new RuntimeException("配置项必须为static变量");
				}
				ConfPath confPath = field.getAnnotation(ConfPath.class);
				String[] props = confPath.value().split("\\.");
				putInConfCore(o, props, field);
			} else if (field.isAnnotationPresent(SystemConfPath.class)) {
				// static检查
				if ((field.getModifiers() & 8) == 0) {
					throw new RuntimeException("配置项必须为static变量");
				}
				SystemConfPath systemConfPath = field.getAnnotation(SystemConfPath.class);
				String[] systemProps = systemConfPath.value().split("\\.");
				putInConfCore(ConfFilter.getSystemMap(), systemProps, field);
			}
		}
	}

	/**
	 * 配置入属性核心处理
	 * 
	 * @param o
	 *            配置数据
	 * @param props
	 *            配置属性key
	 * @param field
	 *            作用域
	 */
	private static void putInConfCore(Object o, String[] props, Field field) {
		int ind = 0;
		while (true) {
			if (ind < props.length && null != o && o instanceof Map) {
				o = ((Map<?, ?>) o).get(props[ind]);
			} else {
				break;
			}
			ind++;
		}
		try {
			field.setAccessible(true);
			//TODO 适合且仅适合String字段 ，可以根据field类型进行转换[boolean int long double float byte short char String]
			field.set(field, String.valueOf(o));
		} catch (ExceptionInInitializerError e) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
