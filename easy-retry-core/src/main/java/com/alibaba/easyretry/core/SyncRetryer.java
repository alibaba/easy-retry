package com.alibaba.easyretry.core;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import com.alibaba.easyretry.common.SCallable;
import com.alibaba.easyretry.common.processor.SyncProcessor;
import com.alibaba.easyretry.common.retryer.Retryer;
import com.alibaba.easyretry.common.retryer.RetryerInfo;
import com.alibaba.easyretry.common.way.SyncRetryWay;
import com.alibaba.easyretry.core.process.sync.ExceptionSyncRetryProcessorr;
import com.alibaba.easyretry.core.process.sync.ResultSyncRetryProcessor;
import com.google.common.util.concurrent.Uninterruptibles;

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
		SyncRetryWay syncRetryWay =
			retryerInfo.getRetryConfiguration().getSyncRetryWayAccess().getCurrentGlobalSyncRetryWay();
		return (V) syncRetryWay.call(callable, retryerInfo);
	}

}
