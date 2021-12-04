package com.alibaba.easyretry.core.utils.json;

import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JsonFactory {

	private JsonFactory() {
	}

	public static JsonAdapter getImpl() {
		return Arrays.stream(JsonSupportEnum.values())
			.filter(
				j -> exist(j.mainClassName)
			).map(
				j -> j.impl.get()
			).findFirst().orElseThrow(
				UnsupportedOperationException::new
			);
	}

	public static JsonAdapter getImpl(JsonSupportEnum jsonSupportEnum) {
		switch (jsonSupportEnum) {
			case GSON:
				return new GsonImpl();
			case JACKSON:
				return new JacksonImpl();
			case FASTJSON:
				return new FastjsonImpl();
			default:
				return getImpl();
		}
	}

	private static boolean exist(String className) {
		try {
			Class.forName(className, false, JsonFactory.class.getClassLoader());
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}
}
