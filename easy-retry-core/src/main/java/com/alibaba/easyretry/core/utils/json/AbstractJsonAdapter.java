package com.alibaba.easyretry.core.utils.json;

import java.util.Objects;
import java.util.function.Supplier;

import com.alibaba.easyretry.core.utils.RetrySpringUtils;

abstract class AbstractJsonAdapter implements JsonAdapter {

	private static Object INSTANCE;

	private static synchronized <T> T create(Supplier<T> create) {
		if (Objects.isNull(INSTANCE)) {
			INSTANCE = create.get();
		}
		return (T) INSTANCE;
	}

	public static <T> T getInstanceFromSpringIfExist(Class<T> clazz, Supplier<T> create) {
		return RetrySpringUtils.getBean(clazz)
			.orElseGet(() -> AbstractJsonAdapter.create(create));
	}
}
