package com.alibaba.easyretry.common.event.on;

import com.alibaba.easyretry.common.event.RetryEvent;

/**
 * @author Created by wuhao on 2021/3/25.
 */
public abstract class OnRetryEvent implements RetryEvent {

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

}
