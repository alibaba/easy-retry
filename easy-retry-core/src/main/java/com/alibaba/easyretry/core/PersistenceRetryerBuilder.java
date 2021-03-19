package com.alibaba.easyretry.core;

import com.alibaba.easyretry.common.ResultPredicate;
import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.retryer.RetryerInfo;

/**
 * @author Created by wuhao on 2020/11/1.
 */
public class PersistenceRetryerBuilder {

	private PersistenceRetryer persistenceRetryer;

	public static PersistenceRetryerBuilder of() {
		return new PersistenceRetryerBuilder();
	}

	public PersistenceRetryerBuilder() {
		RetryerInfo retryerInfo = new RetryerInfo();
		persistenceRetryer = new PersistenceRetryer(retryerInfo);
	}

	public PersistenceRetryerBuilder withExecutorName(String executorName) {
		persistenceRetryer.getRetryerInfo().setExecutorName(executorName);
		return this;
	}

	public PersistenceRetryerBuilder withExecutorMethodName(String executorMethodName) {
		persistenceRetryer.getRetryerInfo().setExecutorMethodName(executorMethodName);
		return this;
	}

	public PersistenceRetryerBuilder withBizId(String bizId) {
		persistenceRetryer.getRetryerInfo().setBizId(bizId);
		return this;
	}

	public PersistenceRetryerBuilder withArgs(Object[] args) {
		persistenceRetryer.getRetryerInfo().setArgs(args);
		return this;
	}

	public PersistenceRetryerBuilder withOnException(Class<? extends Throwable> onException) {
		persistenceRetryer.getRetryerInfo().setOnException(onException);
		return this;
	}

	public PersistenceRetryerBuilder withOnFailureMethod(String onFailureMethod) {
		persistenceRetryer.getRetryerInfo().setOnFailureMethod(onFailureMethod);
		return this;
	}

	public PersistenceRetryerBuilder withReThrowException(boolean reThrowException) {
		persistenceRetryer.getRetryerInfo().setReThrowException(reThrowException);
		return this;
	}

	public PersistenceRetryerBuilder withNamespace(String namespace) {
		persistenceRetryer.getRetryerInfo().setNamespace(namespace);
		return this;
	}

	public PersistenceRetryerBuilder withConfiguration(RetryConfiguration retryConfiguration) {
		persistenceRetryer.getRetryerInfo().setRetryConfiguration(retryConfiguration);
		return this;
	}

	public PersistenceRetryerBuilder withResultPredicate(ResultPredicate resultPredicate) {
		persistenceRetryer.getRetryerInfo().setResultPredicate(resultPredicate);
		return this;
	}


	public PersistenceRetryer build() {
		return persistenceRetryer;
	}
}
