package com.alibaba.easyretry.extension.spring.aop;

import com.alibaba.easyretry.common.PersistenceRetryer;
import com.alibaba.easyretry.common.PersistenceRetryerBuilder;
import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.RetryIdentify;

import lombok.Setter;
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
        MethodSignature signature = (MethodSignature)invocation.getSignature();
        PersistenceRetryer persistenceRetryer = PersistenceRetryerBuilder.of()
            .withExecutorName(getBeanId(signature.getDeclaringType()))
            .withExecutorMethodName(signature.getMethod().getName())
            .withArgs(invocation.getArgs())
            .withConfiguration(retryConfiguration)
            .withOnFailureMethod(retryable.onFailureMethod()).withNamespace(namespace).build();
        persistenceRetryer.call(invocation::proceed);
        return null;
    }

    private String getBeanId(Class<?> type) {
        String[] names = applicationContext.getBeanNamesForType(type);
        return null != names && names.length > 0 ? names[0] : null;
    }
}
