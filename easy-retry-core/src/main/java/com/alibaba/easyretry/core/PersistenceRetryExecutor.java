package com.alibaba.easyretry.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import com.alibaba.easyretry.common.Invocation;
import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.RetryExecutor;
import com.alibaba.easyretry.common.access.RetryTaskAccess;
import com.alibaba.easyretry.common.constant.enums.HandleResultEnum;
import com.alibaba.easyretry.common.entity.RetryTask;
import com.alibaba.easyretry.common.event.RetryEvent;
import com.alibaba.easyretry.common.event.on.FailureOnRetryEvent;
import com.alibaba.easyretry.common.event.on.StopOnRetryEvent;
import com.alibaba.easyretry.common.event.on.SuccessOnRetryEvent;
import com.alibaba.easyretry.common.filter.RetryFilterChain;
import com.alibaba.easyretry.common.filter.RetryFilterResponse;
import com.alibaba.easyretry.common.recover.RecoverContext;
import com.alibaba.easyretry.core.context.MaxAttemptsPersistenceRetryContext;
import com.alibaba.easyretry.core.context.PersistenceRetryContext;
import com.alibaba.easyretry.core.filter.RetryFilterChainFactory;
import com.alibaba.easyretry.core.process.async.on.AbstractAsyncPersistenceOnRetryProcessor;
import com.alibaba.easyretry.core.process.async.on.ExceptionPersistenceAsynOnRetryProcessor;
import com.alibaba.easyretry.core.process.async.on.ResultAsynPersistenceOnRetryProcessor;
import com.alibaba.easyretry.core.utils.LogUtils;
import com.alibaba.easyretry.core.utils.PrintUtils;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Created by wuhao on 2021/3/2.
 */
@Slf4j
public class PersistenceRetryExecutor implements RetryExecutor {

	@Setter
	private RetryConfiguration retryConfiguration;

	@Setter
	private RetryFilterChainFactory retryFilterChainFactory;

	@Override
	public HandleResultEnum doExecute(RetryContext context) {
		try {
			PrintUtils.monitorInfo("begin deal", context);
			return handle(context);
		} catch (Throwable e) {
			log.error("Retry invoke failed", e);
			return HandleResultEnum.ERROR;
		}
	}

	private HandleResultEnum handle(RetryContext context) {
		MaxAttemptsPersistenceRetryContext maxAttemptsPersistenceRetryContext
			= (MaxAttemptsPersistenceRetryContext)context;
		if (maxAttemptsPersistenceRetryContext.getWaitStrategy().shouldWait(context)) {
			PrintUtils.monitorInfo("shouldWait", context);
			return HandleResultEnum.WAITING;
		}
		PrintUtils.monitorInfo("handlingRetryTask", context);
		retryConfiguration.getRetryTaskAccess()
			.handlingRetryTask(maxAttemptsPersistenceRetryContext.getRetryTask());
		AbstractAsyncPersistenceOnRetryProcessor<Object> abstractAsynPersistenceOnRetryProcessor;
		try {
			PrintUtils.monitorInfo("beigin excuteMethod", context);
			RetryFilterChain retryFilterChain =
				retryFilterChainFactory.createFilterChain(maxAttemptsPersistenceRetryContext.getInvokeExecutor());
			RetryFilterResponse retryFilterResponse = retryFilterChain.invoke(context);
			abstractAsynPersistenceOnRetryProcessor = new ResultAsynPersistenceOnRetryProcessor<>(
				retryFilterResponse.getResponse(),
				maxAttemptsPersistenceRetryContext);
			PrintUtils.monitorInfo("excuteMethod success ", context);
		} catch (Throwable t) {
			Throwable throwable = t;
			if (throwable instanceof InvocationTargetException) {
				throwable = throwable.getCause();
			}
			log.error("excuteMethod failed task arg is {} task id is {}", context.getInvocation(), context.getId(), throwable);
			abstractAsynPersistenceOnRetryProcessor = new ExceptionPersistenceAsynOnRetryProcessor<>(
				throwable,
				maxAttemptsPersistenceRetryContext);
		}
		abstractAsynPersistenceOnRetryProcessor.process();
		HandleResultEnum handleResult = abstractAsynPersistenceOnRetryProcessor.getRetryResult();
		PrintUtils.monitorInfo("handleResult ", context, "handleResult is " + handleResult);
		RetryEvent onRetryEvent = null;
		switch (handleResult) {
			case SUCCESS:
				finish(context);
				onRetryEvent = new SuccessOnRetryEvent();
				break;
			case STOP:
				stop(context);
				onRetryEvent = new StopOnRetryEvent();
				break;
			case FAILURE:
				onRetryEvent = new FailureOnRetryEvent();
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + handleResult);
		}
		retryConfiguration.getRetryEventMulticaster().multicast(onRetryEvent);
		return handleResult;
	}

	private void finish(RetryContext context) {
		MaxAttemptsPersistenceRetryContext maxAttemptsPersistenceRetryContext
			= (MaxAttemptsPersistenceRetryContext)context;
		RetryTask retryTask = maxAttemptsPersistenceRetryContext.getRetryTask();
		try {
			RetryTaskAccess retryTaskAccess = retryConfiguration.getRetryTaskAccess();
			retryTaskAccess.finishRetryTask(retryTask);
			context.stop();
		} catch (Throwable t) {
			LogUtils.CONSISTENCY_LOGGER
				.error("finishRetryTask error " + context.getInvocation() + " please check", t);
		}
	}

	private void stop(RetryContext context) {
		try {
			PersistenceRetryContext maxAttemptsPersistenceRetryContext
				= (PersistenceRetryContext)context;
			RetryTaskAccess retryTaskAccess = retryConfiguration.getRetryTaskAccess();
			retryTaskAccess.stopRetryTask(maxAttemptsPersistenceRetryContext.getRetryTask());
			recover(maxAttemptsPersistenceRetryContext);
			context.stop();
		} catch (Throwable t) {
			LogUtils.CONSISTENCY_LOGGER
				.error("stopRetryTask error " + context.getInvocation() + " please check", t);
		}
	}

	private void recover(PersistenceRetryContext context) {
		String methodName = context.getOnFailureMethod();
		if (StringUtils.isEmpty(methodName)) {
			return;
		}
		Invocation invocation = context.getInvocation();
		RecoverContext recoverContext = new RecoverContext() {
			@Override
			public Object[] getArgs() {
				return invocation.getArgs();
			}
		};
		Object executor = invocation.getExecutor();
		Method method = MethodUtils.getMatchingMethod(executor.getClass(), methodName, RecoverContext.class);
		try {
			method.invoke(executor, recoverContext);
		} catch (Throwable t) {
			LogUtils.CONSISTENCY_LOGGER.error("executeOnFailureMethod failed onFailureMethod {} " +
				"invocation is {}", methodName, invocation, t);
		}
	}
}
