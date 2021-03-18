package com.alibaba.easyretry.common;

/**
 * @author Created by wuhao on 2020/11/1.
 */
public class PersistenceRetryerBuilder {

	private PersistenceRetryer persistenceRetryer;

	public static PersistenceRetryerBuilder of() {
		return new PersistenceRetryerBuilder();
	}

	public PersistenceRetryerBuilder() {
		persistenceRetryer = new PersistenceRetryer();
	}

	public PersistenceRetryerBuilder withExecutorName(String executorName) {
		persistenceRetryer.setExecutorName(executorName);
		return this;
	}

	public PersistenceRetryerBuilder withExecutorMethodName(String executorMethodName) {
		persistenceRetryer.setExecutorMethodName(executorMethodName);
		return this;
	}

	public PersistenceRetryerBuilder withBizId(String bizId) {
		persistenceRetryer.setBizId(bizId);
		return this;
	}

	public PersistenceRetryerBuilder withArgs(Object[] args) {
		persistenceRetryer.setArgs(args);
		return this;
	}

	public PersistenceRetryerBuilder withOnException(Class<? extends Throwable> onException) {
		persistenceRetryer.setOnException(onException);
		return this;
	}

	public PersistenceRetryerBuilder withOnFailureMethod(String onFailureMethod) {
		persistenceRetryer.setOnFailureMethod(onFailureMethod);
		return this;
	}

	public PersistenceRetryerBuilder withReThrowException(boolean reThrowException) {
		persistenceRetryer.setReThrowException(reThrowException);
		return this;
	}

	public PersistenceRetryerBuilder withNamespace(String namespace) {
		persistenceRetryer.setNamespace(namespace);
		return this;
	}

	public PersistenceRetryerBuilder withConfiguration(RetryConfiguration retryConfiguration) {
		persistenceRetryer.setRetryConfiguration(retryConfiguration);
		return this;
	}

	public PersistenceRetryerBuilder withResultPredicate(ResultPredicate resultPredicate) {
		persistenceRetryer.setResultPredicate(resultPredicate);
		return this;
	}


	public PersistenceRetryer build() {
		return persistenceRetryer;
	}
}
