package com.alibaba.easyretry.core;


import com.alibaba.easyretry.common.AbstractRetrySyncExecutor;
import com.alibaba.easyretry.common.SCallable;
import com.alibaba.easyretry.common.retryer.Retryer;
import com.alibaba.easyretry.common.retryer.RetryerInfo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Created by zhangchi Created on 2023-07-12
 */
@Slf4j
@Data
public class SyncRetryer<V> implements Retryer<V> {

	private RetryerInfo<V> retryerInfo;

	public SyncRetryer(RetryerInfo<V> retryerInfo) {
		this.retryerInfo = retryerInfo;
	}

	@Override
	public V call(SCallable<V> callable) throws Throwable {
		AbstractRetrySyncExecutor retrySyncExecutor = retryerInfo.getRetryConfiguration().getRetrySyncExecutor();
		retrySyncExecutor.setRetryerInfo(retryerInfo);
		return (V) retrySyncExecutor.call(callable);
	}

}
