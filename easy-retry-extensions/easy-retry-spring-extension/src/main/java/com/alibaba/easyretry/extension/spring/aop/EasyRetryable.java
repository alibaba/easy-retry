package com.alibaba.easyretry.extension.spring.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EasyRetryable {

	/**
	 * 处理完成以后是否需要把异常重新抛出
	 *
	 * @return 是否需要抛出异
	 */
	boolean reThrowException() default false;

	/**
	 * 通过结果判断是否重试
	 *
	 * @return SpEL表达式 参考:https://docs.spring.io/spring-framework/docs/3.0.x/reference/expressions.html
	 */
	String resultCondition() default "";

	/**
	 * 当重试失败的时候
	 * @return
	 */
	String recoverMethod() default "";

}
