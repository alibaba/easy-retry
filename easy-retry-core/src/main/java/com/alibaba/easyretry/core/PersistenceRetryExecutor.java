package com.alibaba.easyretry.core;

import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.RetryExecutor;
import com.alibaba.easyretry.common.access.RetryTaskAccess;
import com.alibaba.easyretry.common.constant.enums.HandleResultEnum;
import com.alibaba.easyretry.common.entity.RetryTask;
import com.alibaba.easyretry.common.filter.RetryFilterInvocation;
import com.alibaba.easyretry.common.filter.RetryFilterInvocationHandler;
import com.alibaba.easyretry.common.filter.RetryFilterResponse;
import com.alibaba.easyretry.core.context.MaxAttemptsPersistenceRetryContext;
import com.alibaba.easyretry.core.process.asyn.on.AbstractAsynPersistenceOnRetryProcessor;
import com.alibaba.easyretry.core.process.asyn.on.ExceptionPersistenceAsynOnRetryProcessor;
import com.alibaba.easyretry.core.process.asyn.on.ResultAsynPersistenceOnRetryProcessor;
import com.alibaba.easyretry.core.utils.LogUtils;
import com.alibaba.easyretry.core.utils.PrintUtils;
import java.lang.reflect.InvocationTargetException;
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
		MaxAttemptsPersistenceRetryContext maxAttemptsPersistenceRetryContext = (MaxAttemptsPersistenceRetryContext) context;
		if (maxAttemptsPersistenceRetryContext.getWaitStrategy().shouldWait(context)) {
			PrintUtils.monitorInfo("shouldWait", context);
			return HandleResultEnum.WAITING;
		}
		PrintUtils.monitorInfo("handlingRetryTask", context);
		retryConfiguration.getRetryTaskAccess()
			.handlingRetryTask(maxAttemptsPersistenceRetryContext.getRetryTask());
		AbstractAsynPersistenceOnRetryProcessor<Object> abstractAsynPersistenceOnRetryProcessor;
		try {
			PrintUtils.monitorInfo("beigin executeMethod", context);
			RetryFilterResponse retryFilterResponse = retryFilterInvocation.invoke(context);
			abstractAsynPersistenceOnRetryProcessor = new ResultAsynPersistenceOnRetryProcessor<>(
				retryFilterResponse.getResponse(),
				maxAttemptsPersistenceRetryContext);
			PrintUtils.monitorInfo("ecuteMethod success ", context);
		} catch (Throwable t) {
			if (t instanceof InvocationTargetException) {
				t = t.getCause();
			}
			log.error("ecuteMethod failed task arg is {} task id is {}", context.getInvocation(),
				context.getId(), t);
			abstractAsynPersistenceOnRetryProcessor = new ExceptionPersistenceAsynOnRetryProcessor<>(
				t,
				maxAttemptsPersistenceRetryContext);
		}
		abstractAsynPersistenceOnRetryProcessor.process();
		HandleResultEnum handleResult = abstractAsynPersistenceOnRetryProcessor.getRetryResult();
		PrintUtils.monitorInfo("handleResult ", context, "handleResult is " + handleResult);
		switch (handleResult) {
			case SUCCESS:
				finish(context);
				break;
			case STOP:
				stop(context);
				break;
			case FAILURE:
		}
		return handleResult;
	}

	private void finish(RetryContext context) {
		MaxAttemptsPersistenceRetryContext maxAttemptsPersistenceRetryContext = (MaxAttemptsPersistenceRetryContext) context;
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
			MaxAttemptsPersistenceRetryContext maxAttemptsPersistenceRetryContext = (MaxAttemptsPersistenceRetryContext) context;
			RetryTaskAccess retryTaskAccess = retryConfiguration.getRetryTaskAccess();
			retryTaskAccess.stopRetryTask(maxAttemptsPersistenceRetryContext.getRetryTask());
			//executeOnFailureMethod(context);
			context.stop();
		} catch (Throwable t) {
			LogUtils.CONSISTENCY_LOGGER
				.error("stopRetryTask error " + context.getInvocation() + " please check", t);
		}
	}

	//private void executeOnFailureMethod(RetryContext context) {
	//
	//	MaxRetryTimesRetryContext maxRetryTimesRetryContext = (MaxRetryTimesRetryContext)context;
	//	String methodName = maxRetryTimesRetryContext.getOnFailureMethod();
	//	if (StringUtils.isEmpty(methodName)) {
	//		return;
	//	}
	//	Object executor = context.getExecutor();
	//	Method onFailure = BeanUtils.getMethod(methodName, executor.getClass());
	//	if (Objects.isNull(onFailure)) {
	//		return;
	//	}
	//	try {
	//		onFailure.invoke(executor, context);
	//	} catch (Throwable t) {
	//		LogUtils.CONSISTENCY_LOGGER.error(
	//			"executeOnFailureMethod failed onFailureMethod = {}"
	//				+ PrintUtils.printCommonMethodInfo(context)
	//				+ " please check",
	//			methodName,
	//			t);
	//	}
	//}
}
