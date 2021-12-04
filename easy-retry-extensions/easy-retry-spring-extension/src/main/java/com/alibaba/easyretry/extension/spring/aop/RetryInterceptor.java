package com.alibaba.easyretry.extension.spring.aop;

import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.RetryIdentify;
import com.alibaba.easyretry.core.PersistenceRetryer;
import com.alibaba.easyretry.core.PersistenceRetryerBuilder;
import com.alibaba.easyretry.extension.spring.SPELResultPredicate;

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

	@Around("@annotation(retryable)")
	public Object around(ProceedingJoinPoint invocation, EasyRetryable retryable) throws Throwable {
		if (RetryIdentify.isOnRetry()) {
			return invocation.proceed();
		}
		MethodSignature signature = (MethodSignature) invocation.getSignature();
		PersistenceRetryerBuilder<Object> builder = PersistenceRetryerBuilder.of(retryConfiguration)
			.withExecutorName(getBeanId(signature.getDeclaringType()))
			.withExecutorMethodName(signature.getMethod().getName())
			.withArgs(invocation.getArgs())
			.withConfiguration(retryConfiguration)
			.withReThrowException(retryable.reThrowException());
		if (StringUtils.isNotBlank(retryable.resultCondition())) {
			builder.withResultCondition(retryable.resultCondition());
		}
		if (StringUtils.isNotBlank(retryable.recoverMethod())) {
			builder.withRecoverMethod(retryable.recoverMethod());
		}
		//		if (StringUtils.isNotBlank(retryable.bizId())) {
		//			SPELParamPredicate param = new SPELParamPredicate(retryable.bizId(),
		//				signature.getMethod());
		//			String bizId = param.apply(invocation.getArgs());
		//			builder.withBizId(bizId);
		//		}
		PersistenceRetryer<Object> persistenceRetryer = builder.build();
		return persistenceRetryer.call(invocation::proceed);
	}

	private String getBeanId(Class<?> type) {
		String[] names = applicationContext.getBeanNamesForType(type);
		return names.length > 0 ? names[0] : null;
	}
}
