package com.alibaba.easyretry.extension.guava;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.alibaba.easyretry.common.AbstractResultPredicate;
import com.alibaba.easyretry.common.AbstractRetrySyncExecutor;
import com.alibaba.easyretry.common.SCallable;
import com.alibaba.easyretry.common.retryer.RetryerInfo;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;

public class GuavaRetrySyncExecutor<V> extends AbstractRetrySyncExecutor<V> {

	private static final long WAIT_TIME = 1000L;

	@Override
	public V call(SCallable<V> callable) throws Throwable {

		RetryerInfo<V> retryerInfo = getRetryerInfo();
		Class<? extends Throwable> onException = Throwable.class;
		if (Objects.nonNull(retryerInfo.getOnException())) {
			onException = retryerInfo.getOnException();
		}

		RetryerBuilder<V> retryerBuilder = RetryerBuilder.<V>newBuilder()
			.retryIfExceptionOfType(onException)
			.withStopStrategy(StopStrategies.stopAfterAttempt(retryerInfo.getRetryConfiguration().getMaxRetryTimes()))
			.withWaitStrategy(WaitStrategies.fixedWait(WAIT_TIME, TimeUnit.MILLISECONDS));

		AbstractResultPredicate<V> resultPredicate = retryerInfo.getResultPredicate();
		if (Objects.nonNull(resultPredicate)) {
			retryerBuilder.retryIfResult(resultPredicate::apply);
		}

		Retryer<V> retryer = retryerBuilder.build();
		return retryer.call(() -> {
			try {
				return callable.call();
			} catch (Throwable t) {
				throw new Exception(t);
			}
		});
	}
}
