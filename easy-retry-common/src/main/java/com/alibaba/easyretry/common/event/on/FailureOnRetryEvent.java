package com.alibaba.easyretry.common.event.on;

import com.alibaba.easyretry.common.RetryContext;

/**
 * @author Created by wuhao on 2021/3/25.
 */
public class FailureOnRetryEvent extends OnRetryEvent {

	public FailureOnRetryEvent(RetryContext retryContext) {
		super(retryContext);
	}

}
