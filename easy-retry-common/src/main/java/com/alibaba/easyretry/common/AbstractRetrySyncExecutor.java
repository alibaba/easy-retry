package com.alibaba.easyretry.common;

import com.alibaba.easyretry.common.retryer.RetryerInfo;

/**
 * @author zhangchi20
 * Created on 2023-07-17
 */
public abstract class AbstractRetrySyncExecutor<V> implements RetrySyncExecutor<V> {

	private RetryerInfo<V> retryerInfo;

	@Override
	public void setRetryerInfo(RetryerInfo<V> retryerInfo) {
		this.retryerInfo = retryerInfo;
	}

	public RetryerInfo<V> getRetryerInfo() {
		return retryerInfo;
	}


	@Override
	public abstract V call(SCallable<V> callable) throws Throwable;

}
