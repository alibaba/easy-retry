package com.alibaba.easyretry.common.event.before;

import java.util.Map;
import java.util.Objects;

import com.alibaba.easyretry.common.entity.RetryTask;
import com.alibaba.easyretry.common.event.RetryEvent;

import com.google.common.collect.Maps;

/**
 * @author Created by wuhao on 2021/3/25.
 */
public abstract class BeforeRetryEvent implements RetryEvent {

	private RetryTask retryTask;

	public BeforeRetryEvent(RetryTask retryTask) {
		this.retryTask = retryTask;
	}

	public boolean isOnRetry() {
		return false;
	}

	public void setAttribute(String key, String value) {
		Map<String, String> extAttrs = retryTask.getExtAttrs();
		if (Objects.isNull(extAttrs)) {
			extAttrs = Maps.newHashMap();
		}
		extAttrs.put(key, value);
	}

	public String getName() {
		return this.getClass().getSimpleName();
	}

}
