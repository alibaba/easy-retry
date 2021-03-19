package com.alibaba.easyretry.core;

import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.RetryExecutor;
import com.alibaba.easyretry.common.RetryIdentify;
import com.alibaba.easyretry.common.access.RetryTaskAccess;
import com.alibaba.easyretry.common.constant.enums.HandleResultEnum;
import com.alibaba.easyretry.common.entity.RetryTask;
import com.alibaba.easyretry.core.process.asyn.AbstractAsynPersistenceOnRetryProcessor;
import com.alibaba.easyretry.core.process.asyn.ExceptionPersistenceAsynOnRetryProcessor;
import com.alibaba.easyretry.core.process.asyn.ResultAsynPersistenceOnRetryProcessor;
import com.alibaba.easyretry.core.utils.BeanUtils;
import com.alibaba.easyretry.core.utils.LogUtils;
import com.alibaba.easyretry.core.utils.PrintUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Created by wuhao on 2021/3/2.
 */
@Slf4j
public class DefaultRetryExecutor implements RetryExecutor {

	@Setter
	private RetryConfiguration retryConfiguration;

	@Override
	public HandleResultEnum doExecute(RetryContext context) {
		try {
			log.info("begin deal arg is {}", context.getArgs());
			return handle(context);
		} catch (Throwable e) {
			log.error("Retry invoke failed", e);
			return HandleResultEnum.ERROR;
		}
	}

	private HandleResultEnum handle(RetryContext context) {
		if (context.getWaitStrategy().shouldWait(context)) {
			return HandleResultEnum.WAITING;
		}
		log.info("handlingRetryTask task arg is {}", context.getArgs());
		retryConfiguration.getRetryTaskAccess().handlingRetryTask(context.getRetryTask());
		AbstractAsynPersistenceOnRetryProcessor abstractAsynPersistenceOnRetryProcessor;
		try {
			RetryIdentify.start();
			log.info("beigin executeMethod task arg is {}", context.getArgs());
			Object result = executeMethod(context);
			abstractAsynPersistenceOnRetryProcessor = new ResultAsynPersistenceOnRetryProcessor(result,context);
			log.info("ecuteMethod success task arg is {}", context.getArgs());
		} catch (Throwable t) {
			if (t instanceof InvocationTargetException) {
				t = t.getCause();
			}
			log.error("ecuteMethod failed task arg is {}", context.getArgs(),t);
			abstractAsynPersistenceOnRetryProcessor = new ExceptionPersistenceAsynOnRetryProcessor(
				t, context);
		} finally {
			RetryIdentify.stop();
		}
		abstractAsynPersistenceOnRetryProcessor.process();
		HandleResultEnum handleResult = abstractAsynPersistenceOnRetryProcessor.getRetryResult();
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

	private Object executeMethod(RetryContext context)
		throws InvocationTargetException, IllegalAccessException {
		Object executor = context.getExecutor();
		return context.getMethod().invoke(executor, context.getArgs());
	}

	private void finish(RetryContext context) {
		RetryTask retryTask = context.getRetryTask();
		try {
			RetryTaskAccess retryTaskAccess = retryConfiguration.getRetryTaskAccess();
			retryTaskAccess.finishRetryTask(retryTask);
			context.stop();
		} catch (Throwable t) {
			LogUtils.CONSISTENCY_LOGGER.error(
				"finishRetryTask error" + PrintUtils.printCommonMethodInfo(context)
					+ " please check", t);
		}
	}

	private void stop(RetryContext context) {
		try {
			RetryTaskAccess retryTaskAccess = retryConfiguration.getRetryTaskAccess();
			retryTaskAccess.stopRetryTask(context.getRetryTask());
			executeOnFailureMethod(context);
			context.stop();
		} catch (Throwable t) {
			LogUtils.CONSISTENCY_LOGGER.error(
				"stopRetryTask error" + PrintUtils.printCommonMethodInfo(context) + " please check",
				t);
		}
	}

	private void executeOnFailureMethod(RetryContext context) {
		String methodName = context.getOnFailureMethod();
		if (StringUtils.isEmpty(methodName)) {
			return;
		}
		Object executor = context.getExecutor();
		Method onFailure = BeanUtils.getMethod(methodName, executor.getClass());
		if (Objects.isNull(onFailure)) {
			return;
		}
		try {
			onFailure.invoke(executor, context);
		} catch (Throwable t) {
			LogUtils.CONSISTENCY_LOGGER.error(
				"executeOnFailureMethod failed onFailureMethod = {}"
					+ PrintUtils.printCommonMethodInfo(context)
					+ " please check",
				methodName,
				t);
		}
	}
}
