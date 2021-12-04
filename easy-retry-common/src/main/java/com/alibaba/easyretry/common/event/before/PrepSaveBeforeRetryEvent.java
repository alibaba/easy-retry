package com.alibaba.easyretry.common.event.before;

import java.util.Map;
import java.util.Objects;

import com.alibaba.easyretry.common.entity.RetryTask;
import com.google.common.collect.Maps;

/**
 * @author Created by wuhao on 2021/3/25.
 */
public class PrepSaveBeforeRetryEvent extends BeforeRetryEvent {

	public PrepSaveBeforeRetryEvent(RetryTask retryTask) {
		super(retryTask);
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}
}
