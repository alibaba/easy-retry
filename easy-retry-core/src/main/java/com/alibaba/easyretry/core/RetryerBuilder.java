package com.alibaba.easyretry.core;

import java.util.Objects;

import com.alibaba.easyretry.common.AbstractResultPredicate;
import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.constant.enums.RetryTypeEnum;
import com.alibaba.easyretry.common.retryer.Retryer;

/**
 * @author Created by zhangchi on 2023-07-13
 */
public class RetryerBuilder<T> {

	/**
	 * 执行者名称
	 */
	private String executorNameContext;

	/**
	 * 执行者方法
	 */
	private String executorMethodNameContext;

	private String onFailureMethodContext;

	/**
	 * 业务id，外部可以自定义存储一些信息
	 */
	private String bizIdContext;

	private Object[] argsContext;

	private Class<? extends Throwable> onExceptionContext;

	private RetryConfiguration retryConfigurationContext;

	private String namespaceContext;

	private boolean reThrowExceptionContext;

	private AbstractResultPredicate<T> resultPredicateContext;

	/**
	 * 重试次数
	 */
	private int retryTimesContext = 5;

	/**
	 * 重试间隔时间
	 */
	private long retryIntervalTimeContext = 0L;


	public RetryerBuilder<T> withExecutorName(String executorName) {
		executorNameContext = executorName;
		return this;
	}

	public RetryerBuilder<T> withExecutorMethodName(String executorMethodName) {
		executorMethodNameContext = executorNameContext;
		return this;
	}

	public RetryerBuilder<T> withBizId(String bizId) {
		bizIdContext = bizId;
		return this;
	}

	public RetryerBuilder<T> withArgs(Object[] args) {
		argsContext = args;
		return this;
	}

	public RetryerBuilder<T> withOnException(Class<? extends Throwable> onException) {
		onExceptionContext = onException;
		return this;
	}

	public RetryerBuilder<T> withOnFailureMethod(String onFailureMethod) {
		onFailureMethodContext = onFailureMethod;
		return this;
	}

	public RetryerBuilder<T> withReThrowException(boolean reThrowException) {
		reThrowExceptionContext = reThrowException;
		return this;
	}

	public RetryerBuilder<T> withNamespace(String namespace) {
		namespaceContext = namespace;
		return this;
	}

	public RetryerBuilder<T> withConfiguration(RetryConfiguration retryConfiguration) {
		retryConfigurationContext = retryConfiguration;
		return this;
	}

	public RetryerBuilder<T> withResultPredicate(AbstractResultPredicate<T> abstractResultPredicate) {
		resultPredicateContext = abstractResultPredicate;
		return this;
	}

	public RetryerBuilder<T> withRetryTimes(Integer retryTimes) {
		retryTimesContext = retryTimes;
		return this;
	}

	public RetryerBuilder<T> withRetryIntervalTime(Long retryIntervalTime) {
		retryIntervalTimeContext = retryIntervalTime;
		return this;
	}

	public Retryer<T> build(RetryTypeEnum retryTypeEnum) {
		if (RetryTypeEnum.SYNC == retryTypeEnum) {
			return buildSyncRetryer();
		} else {
			return buildAsyncRetryer();
		}
	}

	private SyncRetryer<T> buildSyncRetryer() {
		SyncRetryerBuilder<T> builder =
			(SyncRetryerBuilder<T>) SyncRetryerBuilder.of(retryConfigurationContext)
			.withRetryTimes(retryTimesContext)
			.withRetryIntervalTime(retryIntervalTimeContext);
		return builder.build();
	}

	private PersistenceRetryer<T> buildAsyncRetryer() {
		PersistenceRetryerBuilder<T> builder =
			(PersistenceRetryerBuilder<T>) PersistenceRetryerBuilder.of(retryConfigurationContext)
			.withExecutorName(executorNameContext)
			.withExecutorMethodName(executorMethodNameContext)
			.withArgs(argsContext)
			.withConfiguration(retryConfigurationContext)
			//			.withOnFailureMethod(retryable.onFailureMethod())
			//			.withNamespace(namespace)
			.withReThrowException(reThrowExceptionContext)
			.withResultPredicate((AbstractResultPredicate<Object>) resultPredicateContext);
		return builder.build();
	}

}
