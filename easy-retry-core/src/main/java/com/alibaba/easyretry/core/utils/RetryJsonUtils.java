package com.alibaba.easyretry.core.utils;

import java.util.Objects;

import com.alibaba.easyretry.core.utils.json.JsonAdapter;
import com.alibaba.easyretry.core.utils.json.JsonFactory;

public final class RetryJsonUtils {

	private static JsonAdapter ADAPTER;

	private RetryJsonUtils() {
	}

	public static String toJSONString(Object object) {
		return getAdapter().toJSONString(object);
	}

	public static <T> T parseObject(String text, Class<T> clazz) {
		return getAdapter().parseObject(text, clazz);
	}

	private static synchronized JsonAdapter getAdapter() {
		if (Objects.isNull(ADAPTER)) {
			ADAPTER = JsonFactory.getImpl();
		}
		return ADAPTER;
	}
}
