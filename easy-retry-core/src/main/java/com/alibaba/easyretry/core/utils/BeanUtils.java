package com.alibaba.easyretry.core.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class BeanUtils {

	private static Map<Class<?>, Method[]> methodCache = new HashMap<>();

	public static Method getMethod(String methodName, Class<?> cl) {
		if(cl == null || StringUtils.isBlank(methodName)) {
			return null;
		}

		Method[] methods = getMethods(cl);
		for(Method method : methods) {
			if(method.getName().equals(methodName)) {
				return method;
			}
		}
		return null;
	}

	private static Method[] getMethods(Class<?> targetClass) {
		Method[] methods = methodCache.get(targetClass);
		if(methods == null) {
			synchronized (targetClass) {
				methods = targetClass.getMethods();
				methodCache.put(targetClass, methods);
			}
		}
		return methods;
	}
}
