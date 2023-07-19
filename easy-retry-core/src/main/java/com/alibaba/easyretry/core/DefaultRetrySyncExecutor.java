package com.alibaba.easyretry.core;

import com.alibaba.easyretry.common.SCallable;
import com.alibaba.easyretry.common.retryer.RetryerInfo;
import com.alibaba.easyretry.common.RetrySyncExecutor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangchi20 <zhangchi20@kuaishou.com>
 * Created on 2023-07-17
 */
@Slf4j
public class DefaultRetrySyncExecutor<V> implements RetrySyncExecutor<V> {

	protected RetryerInfo<V> retryerInfo;

	@Override
	public V call(SCallable<V> callable) throws Throwable {
		return null;
	}

	@Override
	public void setRetryerInfo(RetryerInfo<V> retryerInfo) {

	}
}
