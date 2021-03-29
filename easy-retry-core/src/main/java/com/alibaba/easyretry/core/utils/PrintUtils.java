package com.alibaba.easyretry.core.utils;

import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.entity.RetryTask;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Created by wuhao on 2020/11/14.
 */
@Slf4j
public class PrintUtils {

	public static String printCommonMethodInfo(RetryContext context) {
		RetryTask retryTask = context.getRetryTask();
		return String.format(
			"executeMethod failed id = %s executorName= %s executorMethodName = %s args = %s",
			retryTask.getId(),
			retryTask.getExecutorName(),
			retryTask.getExecutorMethodName(),
			Arrays.toString(context.getArgs()));
	}


	public static void monitorInfo(String action,RetryContext context) {
		monitorInfo(action, context, "");
	}

	public static void monitorInfo(String action,RetryContext context,String extraInfo) {
		log.info(action + " arg is {} task id is {} " + extraInfo, context.getArgs(),
			context.getRetryTask().getId());
	}
}
