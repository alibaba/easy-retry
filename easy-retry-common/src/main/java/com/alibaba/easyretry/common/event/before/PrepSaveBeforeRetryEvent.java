package com.alibaba.easyretry.common.event.before;

import com.alibaba.easyretry.common.entity.RetryTask;

/**
 * @author Created by wuhao on 2021/3/25.
 */
public class PrepSaveBeforeRetryEvent extends BeforeRetryEvent {

	public PrepSaveBeforeRetryEvent(RetryTask retryTask) {
		super(retryTask);
	}
}
