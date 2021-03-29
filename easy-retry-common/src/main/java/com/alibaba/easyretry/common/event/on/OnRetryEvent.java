package com.alibaba.easyretry.common.event.on;

import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.event.RetryEvent;

/**
 * @author Created by wuhao on 2021/3/25.
 */
public abstract class OnRetryEvent implements RetryEvent {

	private RetryContext retryContext;

	public OnRetryEvent(RetryContext retryContext) {
		this.retryContext = retryContext;
	}

	public void setAttribute(String key, String value) {
		retryContext.setAttribute(key, value);
	}

	public String getAttribute(String key) {
		return retryContext.getAttribute(key);
	}

	public boolean isOnRetry() {
		return true;
	}

	public String getName() {
		return this.getClass().getSimpleName();
	}

}
