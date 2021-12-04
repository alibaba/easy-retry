package com.alibaba.easyretry.common.event.on;

import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.event.RetryEvent;

/**
 * @author Created by wuhao on 2021/3/25.
 */
public abstract class OnRetryEvent implements RetryEvent {

	final private RetryContext retryContext;

	public OnRetryEvent(RetryContext retryContext) {
		this.retryContext = retryContext;
	}

	@Override
	public void setAttribute(String key, String value) {
		retryContext.setAttribute(key, value);
	}

	public String getAttribute(String key) {
		return retryContext.getAttribute(key);
	}

	@Override
	public boolean isOnRetry() {
		return true;
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

}
