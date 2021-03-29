package com.alibaba.easyretry.core.utils;

import com.alibaba.easyretry.common.RetryContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Created by wuhao on 2020/11/14.
 */
@Slf4j
public class PrintUtils {

	public static void monitorInfo(String action, RetryContext context) {
		monitorInfo(action, context, "");
	}

	public static void monitorInfo(String action, RetryContext context, String extraInfo) {
		log.info(action + " arg is {} task id is {} " + extraInfo, context.getInvocation(),
			context.getId());
	}
}
