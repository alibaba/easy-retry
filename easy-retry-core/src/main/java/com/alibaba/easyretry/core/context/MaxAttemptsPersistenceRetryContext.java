package com.alibaba.easyretry.core.context;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import com.alibaba.easyretry.common.Invocation;
import com.alibaba.easyretry.common.InvokeExecutor;
import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.RetryLifecycle;
import com.alibaba.easyretry.common.SimpleMethodIExecutor;
import com.alibaba.easyretry.common.entity.RetryTask;
import com.alibaba.easyretry.common.predicate.ResultPredicateProduce;
import com.alibaba.easyretry.common.serializer.RetryArgSerializer;
import com.alibaba.easyretry.common.strategy.StopStrategy;
import com.alibaba.easyretry.common.strategy.WaitStrategy;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.reflect.MethodUtils;

@Data
@ToString(callSuper = true)
public class MaxAttemptsPersistenceRetryContext extends PersistenceRetryContext implements RetryContext,
	RetryLifecycle {

	private int maxRetryTimes;

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

	public static class MaxAttemptsRetryContextBuilder extends RetryContextBuilder{

		public MaxAttemptsRetryContextBuilder(RetryConfiguration retryConfiguration, RetryTask retryTask) {
			super(retryConfiguration,retryTask,new MaxAttemptsPersistenceRetryContext());
		}
		@Override
		public PersistenceRetryContext build() {
			PersistenceRetryContext retryContext = super.build();
			MaxAttemptsPersistenceRetryContext maxContext = (MaxAttemptsPersistenceRetryContext) retryContext;
			maxContext.setMaxRetryTimes(retryConfiguration.getMaxRetryTimes());
			return retryContext;
		}
	}
}
