package com.alibaba.easyretry.common.event.before;

import com.alibaba.easyretry.common.entity.RetryTask;

/**
 * @author Created by wuhao on 2021/3/25.
 */
public class AfterSaveBeforeRetryEvent extends BeforeRetryEvent {

	public AfterSaveBeforeRetryEvent(RetryTask retryTask) {
		super(retryTask);
	}
}
