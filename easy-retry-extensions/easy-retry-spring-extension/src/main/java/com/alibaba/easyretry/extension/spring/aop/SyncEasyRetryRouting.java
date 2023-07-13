package com.alibaba.easyretry.extension.spring.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SyncEasyRetryRouting {

	/**
	 * 重试次数，目前同步使用
	 * 默认5次
	 */
	int retryTimes() default 5;

	/**
	 * 重试间隔时间
	 */
	long retryIntervalTime() default 0;

}
