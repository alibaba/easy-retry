package com.alibaba.easyretry.core.context;

import com.alibaba.easyretry.common.Invocation;
import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.RetryLifecycle;
import com.alibaba.easyretry.common.SimpleMethodInvocation;
import com.alibaba.easyretry.common.entity.RetryTask;
import com.alibaba.easyretry.common.serializer.ResultPredicateSerializer;
import com.alibaba.easyretry.common.serializer.RetryArgSerializer;
import com.alibaba.easyretry.common.strategy.StopStrategy;
import com.alibaba.easyretry.common.strategy.WaitStrategy;
import com.google.common.collect.Maps;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.reflect.MethodUtils;

@Data
@ToString(callSuper = true)
public class MaxAttemptsPersistenceRetryContext implements RetryContext, RetryLifecycle,
	Comparable<MaxAttemptsPersistenceRetryContext> {

	private RetryTask retryTask;

	private RetryArgSerializer retryArgSerializer;

	private ResultPredicateSerializer resultPredicateSerializer;

	private Long priority;

	private StopStrategy stopStrategy;

	private WaitStrategy waitStrategy;

	private int maxRetryTimes;

	private String onFailureMethod;

	private Invocation invocation;

	@Override
	public int compareTo(MaxAttemptsPersistenceRetryContext o) {
		return this.priority > o.getPriority() ? 1 : -1;
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
		stopStrategy.clear(this);
		waitStrategy.clear(this);
	}

	public void setAttribute(String key, String value) {
		Map<String, String> extAttrs = retryTask.getExtAttrs();
		if (Objects.isNull(extAttrs)) {
			extAttrs = Maps.newHashMap();
		}
		extAttrs.put(key, value);
	}

	public String getAttribute(String key) {
		Map<String, String> extAttrs = retryTask.getExtAttrs();
		if (Objects.isNull(extAttrs)) {
			return null;
		} else {
			return extAttrs.get(key);
		}
	}

	public Long getNextRetryTime(TimeUnit unit) {
		return unit.convert(priority, TimeUnit.MILLISECONDS);
	}

	public void setNextRetryTime(Long nexRetryTime, TimeUnit unit) {
		priority = unit.toMillis(nexRetryTime);
	}

	@Override
	public String getId() {
		return retryTask.getId() + "";
	}


	public static class RetryContextBuilder {

		private MaxAttemptsPersistenceRetryContext retryContext;

		private RetryConfiguration retryConfiguration;

		private RetryTask retryTask;

		public RetryContextBuilder(RetryConfiguration retryConfiguration, RetryTask retryTask) {
			retryContext = new MaxAttemptsPersistenceRetryContext();
			this.retryConfiguration = retryConfiguration;
			this.retryTask = retryTask;
		}

		public RetryContextBuilder buildInvocation() {
			RetryArgSerializer retryArgSerializer = retryConfiguration.getRetrySerializerAccess()
				.getCurrentGlobalRetrySerializer();
			Object[] args = retryArgSerializer.deSerialize(retryTask.getArgsStr()).getArgs();
			Object executor = retryConfiguration.getExecutorSolver()
				.resolver(retryTask.getExecutorName());
			Class<?>[] classes = Stream.of(args).map(Object::getClass).toArray(Class[]::new);
			Method method = MethodUtils
				.getMatchingMethod(executor.getClass(), retryTask.getExecutorMethodName(), classes);
			SimpleMethodInvocation simpleMethodInvocation = new SimpleMethodInvocation(executor,
				method, args);
			retryContext.setInvocation(simpleMethodInvocation);
			return this;
		}


		public RetryContextBuilder buildRetryArgSerializer() {
			retryContext.setRetryArgSerializer(
				retryConfiguration.getRetrySerializerAccess().getCurrentGlobalRetrySerializer());
			return this;
		}

		public RetryContextBuilder buildStopStrategy() {
			retryContext.setStopStrategy(
				retryConfiguration.getRetryStrategyAccess().getCurrentGlobalStopStrategy());
			return this;
		}

		public RetryContextBuilder buildWaitStrategy() {
			retryContext.setWaitStrategy(
				retryConfiguration.getRetryStrategyAccess().getCurrentGlobalWaitStrategy());
			return this;
		}

		public RetryContextBuilder buildRetryTask() {
			retryContext.setRetryTask(retryTask);
			return this;
		}

		public RetryContextBuilder buildMaxRetryTimes() {
			retryContext.setMaxRetryTimes(retryConfiguration.getMaxRetryTimes());
			return this;
		}

		public RetryContextBuilder buildOnFailureMethod() {
			retryContext.setOnFailureMethod(retryTask.getOnFailureMethod());
			return this;
		}

		public RetryContextBuilder buildResultPredicateSerializer() {
			retryContext
				.setResultPredicateSerializer(retryConfiguration.getResultPredicateSerializer());
			return this;
		}

		public RetryContextBuilder buildPriority(Long priority) {
			retryContext.setPriority(priority);
			return this;
		}

		public MaxAttemptsPersistenceRetryContext build() {
			return retryContext;
		}
	}
}
