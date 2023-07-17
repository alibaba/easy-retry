package com.alibaba.easyretry.core.way;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.Objects;

import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.SCallable;
import com.alibaba.easyretry.common.processor.SyncProcessor;
import com.alibaba.easyretry.common.retryer.RetryerInfo;
import com.alibaba.easyretry.common.way.SyncRetryWay;
import com.alibaba.easyretry.core.process.sync.ExceptionSyncRetryProcessorr;
import com.alibaba.easyretry.core.process.sync.ResultSyncRetryProcessor;
import com.google.common.util.concurrent.Uninterruptibles;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangchi20 <zhangchi20@kuaishou.com>
 * Created on 2023-07-17
 */
@Slf4j
public class DefaultSyncRetryWay<V> implements SyncRetryWay<V> {

	private static final Long INTERNAL_TIME = 1000L;
	private static final Integer RETRY_TIMES = 5;

	@Override
	public V call(SCallable<V> callable, RetryerInfo<V> retryerInfo) throws Throwable {
		Integer maxRetryTimes = RETRY_TIMES;
		if (Objects.nonNull(retryerInfo.getRetryConfiguration())
			&& Objects.nonNull(retryerInfo.getRetryConfiguration().getMaxRetryTimes())) {
			maxRetryTimes = retryerInfo.getRetryConfiguration().getMaxRetryTimes();
		}

		SyncProcessor<V> syncProcessor;
		int retryCount = 0;
		while (retryCount++ < maxRetryTimes) {
			try {
				V result =  callable.call();
				syncProcessor = new ResultSyncRetryProcessor<>(result, retryerInfo);
				return syncProcessor.getResult();
			} catch (Throwable throwable) {
				log.error("[sync retry] call method error retryCount is {}", retryCount, throwable);

				if (retryCount > 0) {
					Uninterruptibles.sleepUninterruptibly(INTERNAL_TIME, MILLISECONDS);
				}

				if (retryCount == maxRetryTimes) {
					syncProcessor = new ExceptionSyncRetryProcessorr<>(throwable, retryerInfo);
					return syncProcessor.getResult();
				}
			}
		}
		return null;
	}
}
