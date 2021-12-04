package com.alibaba.easyretry.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;

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
import com.alibaba.easyretry.common.filter.RetryFilterInvocation;
import com.alibaba.easyretry.common.filter.RetryFilterResponse;
import com.alibaba.easyretry.common.recover.RecoverContext;
import com.alibaba.easyretry.core.context.MaxAttemptsPersistenceRetryContext;
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
	private RetryFilterInvocation retryFilterInvocation;

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
			RetryFilterResponse retryFilterResponse = retryFilterInvocation.invoke(context);
			abstractAsynPersistenceOnRetryProcessor = new ResultAsynPersistenceOnRetryProcessor<>(
				retryFilterResponse.getResponse(),
				maxAttemptsPersistenceRetryContext);
			PrintUtils.monitorInfo("excuteMethod success ", context);
		} catch (Throwable t) {
			Throwable throwable = t;
			if (throwable instanceof InvocationTargetException) {
				throwable = throwable.getCause();
			}
			log.error("excuteMethod failed task arg is {} task id is {}", context.getInvocation(),
				context.getId(), throwable);
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
			MaxAttemptsPersistenceRetryContext maxAttemptsPersistenceRetryContext
				= (MaxAttemptsPersistenceRetryContext)context;
			RetryTaskAccess retryTaskAccess = retryConfiguration.getRetryTaskAccess();
			retryTaskAccess.stopRetryTask(maxAttemptsPersistenceRetryContext.getRetryTask());
			recover(context);
			context.stop();
		} catch (Throwable t) {
			LogUtils.CONSISTENCY_LOGGER
				.error("stopRetryTask error " + context.getInvocation() + " please check", t);
		}
	}

	private void recover(RetryContext context) {
		MaxAttemptsPersistenceRetryContext maxAttemptsPersistenceRetryContext = (MaxAttemptsPersistenceRetryContext) context;
		String methodName = maxAttemptsPersistenceRetryContext.getOnFailureMethod();
		if (StringUtils.isEmpty(methodName)) {
			return;
		}
		RecoverContext recoverContext = new RecoverContext() {
			@Override
			public Object[] getArgs() {
				return new Object[0];
			}

			@Override
			public Throwable getThrowable() {
				return null;
			}
		};

//		context.getInvocation().invokeMethod()
//
//		Object executor = context.getExecutor();
//		Method onFailure = BeanUtils.getMethod(methodName, executor.getClass());
//		if (Objects.isNull(onFailure)) {
//			return;
//		}
//		try {
//			onFailure.invoke(executor, context);
//		} catch (Throwable t) {
//			LogUtils.CONSISTENCY_LOGGER.error(
//				"executeOnFailureMethod failed onFailureMethod = {}"
//					+ PrintUtils.printCommonMethodInfo(context)
//					+ " please check",
//				methodName,
//				t);
//		}
	}
}
