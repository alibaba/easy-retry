package com.alibaba.easyretry.core;

import java.util.Objects;

import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.retryer.RetryerInfo;

/**
 * @author Created by zhangchi on 2023-07-12
 */
public class SyncRetryerBuilder<T> {

	private SyncRetryer<T> syncRetryer;

	public SyncRetryerBuilder(RetryConfiguration retryConfiguration) {
		RetryerInfo<T> retryerInfo = new RetryerInfo<>();
		syncRetryer = new SyncRetryer<>(retryerInfo);
	}

	public static <T> SyncRetryerBuilder<T> of(RetryConfiguration retryConfiguration) {
		return new SyncRetryerBuilder<>(retryConfiguration);
	}


	public SyncRetryerBuilder<T> withRetryTimes(Integer retryTimes) {
		int maxRetryTimes = 5;
		if (Objects.nonNull(retryTimes)) {
			maxRetryTimes = retryTimes;
		}
		syncRetryer.getRetryerInfo().setRetryTimes(maxRetryTimes);
		return this;
	}

	public SyncRetryerBuilder<T> withRetryIntervalTime(Long retryIntervalTime) {
		long intervalTime = 0;
		if (Objects.nonNull(retryIntervalTime)) {
			intervalTime = retryIntervalTime;
		}
		syncRetryer.getRetryerInfo().setRetryIntervalTime(intervalTime);
		return this;
	}

	public SyncRetryer<T> build() {
		return syncRetryer;
	}

}
