package com.alibaba.easyretry.core.utils;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class RetrySpringUtils implements ApplicationContextAware {

	private static ApplicationContext CONTEXT;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		RetrySpringUtils.CONTEXT = applicationContext;
	}

	public static boolean inSpringContext() {
		return Objects.nonNull(CONTEXT);
	}

	public static <T> Optional<T> getBean(Class<T> clazz) {
		if (!inSpringContext()) {
			return Optional.empty();
		}
		try {
			return Optional.ofNullable(CONTEXT.getBean(clazz));
		} catch (BeansException e) {
			return Optional.empty();
		}
	}
}
