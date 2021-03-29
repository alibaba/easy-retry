package com.alibaba.easyretry.core.strategy;

import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.strategy.StopStrategy;
import com.alibaba.easyretry.common.strategy.WaitStrategy;
import com.alibaba.easyretry.core.context.MaxAttemptsPersistenceRetryContext;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultRetryStrategy implements StopStrategy, WaitStrategy {

	private static final Long MAX_INTERNAL_TIME = 15 * 60 * 1000L;
	private static final Long BASE_INTERNAL_TIME = 5000L;
	private Map<String, Long> internalTimeMap = Maps.newConcurrentMap();
	private Map<String, Integer> retryTimeMap = Maps.newConcurrentMap();

	@Override
	public boolean shouldStop(RetryContext context) {
		MaxAttemptsPersistenceRetryContext maxAttemptsPersistenceRetryContext = (MaxAttemptsPersistenceRetryContext) context;
		Integer retryTimes = retryTimeMap.get(context.getId());
		if (Objects.isNull(retryTimes)) {
			retryTimes = 1;
		}
		log.warn(
			"shouldStop retryTime is {} id is {} maxRetryTime is {}",
			retryTimes,
			maxAttemptsPersistenceRetryContext.getMaxRetryTimes());
		return retryTimes >= maxAttemptsPersistenceRetryContext.getMaxRetryTimes();
	}

	@Override
	public boolean shouldWait(RetryContext context) {
		MaxAttemptsPersistenceRetryContext maxAttemptsPersistenceRetryContext = (MaxAttemptsPersistenceRetryContext) context;
		internalTimeMap.putIfAbsent(context.getId(), 0L);
		Long priority = maxAttemptsPersistenceRetryContext.getNextRetryTime(TimeUnit.MILLISECONDS);
		if (Objects.isNull(priority)) {
			priority = 0L;
		}
		return System.currentTimeMillis() < priority;
	}

	@Override
	public void backOff(RetryContext context) {
		MaxAttemptsPersistenceRetryContext maxAttemptsPersistenceRetryContext = (MaxAttemptsPersistenceRetryContext) context;

		Integer retryTime = retryTimeMap.get(context.getId());
		Long lastInternalTime = internalTimeMap.get(context.getId());
		if (Objects.isNull(retryTime)) {
			retryTime = 1;
		}
		if (Objects.isNull(lastInternalTime)) {
			lastInternalTime = 0L;
		}
		long nextInternalTime = retryTime * (lastInternalTime + BASE_INTERNAL_TIME);
		nextInternalTime = Math.min(nextInternalTime, MAX_INTERNAL_TIME);

		internalTimeMap.put(context.getId(), nextInternalTime);

		retryTime++;
		retryTimeMap.put(context.getId(), retryTime);
		maxAttemptsPersistenceRetryContext
			.setNextRetryTime(System.currentTimeMillis() + nextInternalTime, TimeUnit.MILLISECONDS);
		log.warn(
			"backOff nextInternalTime is {} id is {} retryTime is {}",
			nextInternalTime,
			context.getId(),
			retryTime);
	}

	@Override
	public void clear(RetryContext context) {
		internalTimeMap.remove(context.getId());
		retryTimeMap.remove(context.getId());
	}
}
