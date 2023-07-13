package com.alibaba.easyretry.core;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import com.alibaba.easyretry.common.SCallable;
import com.alibaba.easyretry.common.processor.SyncProcessor;
import com.alibaba.easyretry.common.retryer.Retryer;
import com.alibaba.easyretry.common.retryer.RetryerInfo;
import com.alibaba.easyretry.core.process.sync.ExceptionSyncRetryProcessorr;
import com.alibaba.easyretry.core.process.sync.ResultSyncRetryProcessor;

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
		SyncProcessor<V> syncProcessor;
		int retryCount = 0;
		while (retryCount++ < retryerInfo.getRetryTimes()) {
			try {
				V result =  callable.call();
				syncProcessor = new ResultSyncRetryProcessor<>(result, retryerInfo);
				return syncProcessor.getResult();
			} catch (Throwable throwable) {
				log.error("[sync retry] call method error retryCount is {}", retryCount, throwable);

				if (retryCount > 0) {
					sleepUninterruptibly(retryerInfo.getRetryIntervalTime(), MILLISECONDS);
				}

				if (retryCount == retryerInfo.getRetryTimes()) {
					syncProcessor = new ExceptionSyncRetryProcessorr<>(throwable, retryerInfo);
					return syncProcessor.getResult();
				}
			}
		}
		return null;
	}

}
