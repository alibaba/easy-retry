package com.alibaba.easyretry.common.event.on;

import com.alibaba.easyretry.common.RetryContext;

/**
 * @author Created by wuhao on 2021/3/25.
 */
public class SuccessOnRetryEvent extends OnRetryEvent {

	public SuccessOnRetryEvent(RetryContext retryContext) {
		super(retryContext);
	}

}
