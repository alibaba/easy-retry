package com.alibaba.easyretry.extension.spring.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EasyRetryable {

	/**
	 * 重试失败后最终调用的方法,方法格式应该为 public void onRetryFailed(RetryContext context)
	 */
	String onFailureMethod() default "";

	/**
	 * 最大重试次数
	 */
	int maxRetryTimes() default 10;

	/**
	 * 处理完成以后是否需要把异常重新抛出
	 *
	 * @return 是否需要抛出异
	 */
	boolean reThrowException() default false;


	/**
	 * 通过结果判断是否重试
	 */
	String resultCondition() default "";


}
