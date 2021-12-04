package com.alibaba.easyretry.core.context;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.apache.commons.lang3.reflect.MethodUtils;

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

@Data
@ToString(callSuper = true)
public class PersistenceRetryContext implements RetryContext, RetryLifecycle,
	Comparable<PersistenceRetryContext> {

	private RetryTask retryTask;

	private RetryArgSerializer retryArgSerializer;

	private Long priority;

	private StopStrategy stopStrategy;

	private WaitStrategy waitStrategy;

	private String onFailureMethod;

	private ResultPredicateProduce resultPredicateProduce;

	private InvokeExecutor invokeExecutor;

	private Invocation invocation;

	@Override
	public int compareTo(PersistenceRetryContext o) {
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

	@Override
	public void setAttribute(String key, String value) {
		Map<String, String> extAttrs = retryTask.getExtAttrs();
		if (Objects.isNull(extAttrs)) {
			extAttrs = Maps.newHashMap();
		}
		extAttrs.put(key, value);
	}

	@Override
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

	@Override
	public Invocation getInvocation() {
		return invocation;
	}

	public static class RetryContextBuilder {

		private PersistenceRetryContext retryContext;

		protected RetryConfiguration retryConfiguration;

		protected RetryTask retryTask;

		public RetryContextBuilder(RetryConfiguration retryConfiguration, RetryTask retryTask) {
			retryContext = new PersistenceRetryContext();
			this.retryConfiguration = retryConfiguration;
			this.retryTask = retryTask;
		}

		public RetryContextBuilder(RetryConfiguration retryConfiguration, RetryTask retryTask, PersistenceRetryContext retryContext) {
			this.retryContext = retryContext;
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
			SimpleMethodIExecutor simpleMethodIExecutor = new SimpleMethodIExecutor(executor,
				method, args);
			retryContext.setInvokeExecutor(simpleMethodIExecutor);

			Invocation simpleInvocation = new Invocation();
			simpleInvocation.setExecutor(executor);
			simpleInvocation.setArgs(args);
			simpleInvocation.setMethod(method);
			retryContext.setInvocation(simpleInvocation);
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


		public RetryContextBuilder buildOnFailureMethod() {
			retryContext.setOnFailureMethod(retryTask.getRecoverMethod());
			return this;
		}

		public RetryContextBuilder buildResultPredicateProduce() {
			retryContext.setResultPredicateProduce(retryConfiguration.getResultPredicateProduce());
			return this;
		}

		public RetryContextBuilder buildPriority(Long priority) {
			retryContext.setPriority(priority);
			return this;
		}

		public PersistenceRetryContext build() {
			return retryContext;
		}
	}
}
