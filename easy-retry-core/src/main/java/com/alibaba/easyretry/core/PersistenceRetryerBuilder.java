package com.alibaba.easyretry.core;

import com.alibaba.easyretry.common.AbstractResultPredicate;
import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.retryer.RetryerInfo;

/**
 * @author Created by wuhao on 2020/11/1.
 */
public class PersistenceRetryerBuilder<T> {

	private PersistenceRetryer<T> persistenceRetryer;

	public PersistenceRetryerBuilder(RetryConfiguration retryConfiguration) {
		RetryerInfo<T> retryerInfo = new RetryerInfo<>();
		persistenceRetryer = new PersistenceRetryer<T>(retryerInfo);
	}

	public static <T> PersistenceRetryerBuilder<T> of(RetryConfiguration retryConfiguration) {
		return new PersistenceRetryerBuilder<>(retryConfiguration);
	}

	public PersistenceRetryerBuilder<T> withExecutorName(String executorName) {
		persistenceRetryer.getRetryerInfo().setExecutorName(executorName);
		return this;
	}

	public PersistenceRetryerBuilder<T> withExecutorMethodName(String executorMethodName) {
		persistenceRetryer.getRetryerInfo().setExecutorMethodName(executorMethodName);
		return this;
	}

	public PersistenceRetryerBuilder<T> withBizId(String bizId) {
		persistenceRetryer.getRetryerInfo().setBizId(bizId);
		return this;
	}

	public PersistenceRetryerBuilder<T> withArgs(Object[] args) {
		persistenceRetryer.getRetryerInfo().setArgs(args);
		return this;
	}

	public PersistenceRetryerBuilder<T> withOnException(Class<? extends Throwable> onException) {
		persistenceRetryer.getRetryerInfo().setOnException(onException);
		return this;
	}

	public PersistenceRetryerBuilder<T> withOnFailureMethod(String onFailureMethod) {
		persistenceRetryer.getRetryerInfo().setOnFailureMethod(onFailureMethod);
		return this;
	}

	public PersistenceRetryerBuilder<T> withReThrowException(boolean reThrowException) {
		persistenceRetryer.getRetryerInfo().setReThrowException(reThrowException);
		return this;
	}

	public PersistenceRetryerBuilder<T> withNamespace(String namespace) {
		persistenceRetryer.getRetryerInfo().setNamespace(namespace);
		return this;
	}

	public PersistenceRetryerBuilder<T> withConfiguration(RetryConfiguration retryConfiguration) {
		persistenceRetryer.getRetryerInfo().setRetryConfiguration(retryConfiguration);
		return this;
	}

	public PersistenceRetryerBuilder<T> withResultPredicate(AbstractResultPredicate<T> abstractResultPredicate) {
		persistenceRetryer.getRetryerInfo().setResultPredicate(abstractResultPredicate);
		return this;
	}


	public PersistenceRetryer<T> build() {
		return persistenceRetryer;
	}
}
