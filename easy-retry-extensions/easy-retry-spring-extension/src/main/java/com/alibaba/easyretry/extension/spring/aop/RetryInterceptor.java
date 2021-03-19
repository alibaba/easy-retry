package com.alibaba.easyretry.extension.spring.aop;

import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.RetryIdentify;
import com.alibaba.easyretry.core.PersistenceRetryer;
import com.alibaba.easyretry.core.PersistenceRetryerBuilder;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;

@Aspect
public class RetryInterceptor {

	@Setter
	private RetryConfiguration retryConfiguration;

	@Setter
	private ApplicationContext applicationContext;

	@Setter
	private String namespace;

	@Around("@annotation(retryable)")
	public Object around(ProceedingJoinPoint invocation, EasyRetryable retryable) throws Throwable {
		if (RetryIdentify.isOnRetry()) {
			return invocation.proceed();
		}
		MethodSignature signature = (MethodSignature) invocation.getSignature();
		PersistenceRetryerBuilder builder = PersistenceRetryerBuilder.of()
			.withExecutorName(getBeanId(signature.getDeclaringType()))
			.withExecutorMethodName(signature.getMethod().getName())
			.withArgs(invocation.getArgs())
			.withConfiguration(retryConfiguration)
			.withOnFailureMethod(retryable.onFailureMethod())
			.withNamespace(namespace)
			.withReThrowException(retryable.reThrowException());
		if (StringUtils.isNotBlank(retryable.resultCondition())) {
			builder.withResultPredicate(new SPELResultPredicate(retryable.resultCondition()));
		}
		PersistenceRetryer persistenceRetryer = builder.build();
		return persistenceRetryer.call(invocation::proceed);
	}

	private String getBeanId(Class<?> type) {
		String[] names = applicationContext.getBeanNamesForType(type);
		return null != names && names.length > 0 ? names[0] : null;
	}
}
