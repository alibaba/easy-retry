package com.alibaba.easyretry.core.utils;

import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.entity.RetryTask;
import java.util.Arrays;

/**
 * @author Created by wuhao on 2020/11/14.
 */
public class PrintUtils {

	public static String printCommonMethodInfo(RetryContext context) {
		RetryTask retryTask = context.getRetryTask();
		return String.format(
			"executeMethod failed executorName=%s  executorMethodName=%s args=%s",
			retryTask.getExecutorName(),
			retryTask.getExecutorMethodName(),
			Arrays.toString(context.getArgs()));
	}
}
