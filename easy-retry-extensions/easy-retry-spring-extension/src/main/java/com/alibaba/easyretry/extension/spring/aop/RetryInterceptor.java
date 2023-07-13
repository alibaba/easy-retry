package com.alibaba.easyretry.extension.spring.aop;

import java.lang.reflect.Method;
import java.util.Objects;

import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.RetryIdentify;
import com.alibaba.easyretry.common.retryer.Retryer;
import com.alibaba.easyretry.core.PersistenceRetryer;
import com.alibaba.easyretry.core.PersistenceRetryerBuilder;
import com.alibaba.easyretry.core.RetryerBuilder;
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

		Retryer<Object> retryer = determineTargetRetryer(invocation, retryable);
		return retryer.call(invocation::proceed);
	}

	private String getBeanId(Class<?> type) {
		String[] names = applicationContext.getBeanNamesForType(type);
		return null != names && names.length > 0 ? names[0] : null;
	}

	private Retryer<Object> determineTargetRetryer(ProceedingJoinPoint invocation, EasyRetryable retryable) {
		MethodSignature signature = (MethodSignature)invocation.getSignature();
		RetryerBuilder<Object> retryerBuilder = new RetryerBuilder<Object>()
			.withExecutorName(getBeanId(signature.getDeclaringType()))
			.withExecutorMethodName(signature.getMethod().getName())
			.withArgs(invocation.getArgs())
			.withConfiguration(retryConfiguration)
			.withReThrowException(retryable.reThrowException());
		if (StringUtils.isNotBlank(retryable.resultCondition())) {
			retryerBuilder.withResultPredicate(new SPELResultPredicate<>(retryable.resultCondition()));
		}

		Method method = signature.getMethod();
		SyncEasyRetryRouting easyRetryRouting = method.getAnnotation(SyncEasyRetryRouting.class);
		if (Objects.nonNull(easyRetryRouting)) {
			retryerBuilder.withRetryTimes(easyRetryRouting.retryTimes())
				.withRetryIntervalTime(easyRetryRouting.retryIntervalTime());
		}
		return retryerBuilder.build(retryable.retryType());
	}
}
