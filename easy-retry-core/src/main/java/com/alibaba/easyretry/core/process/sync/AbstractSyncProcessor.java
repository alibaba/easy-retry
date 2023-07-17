package com.alibaba.easyretry.core.process.sync;

import com.alibaba.easyretry.common.processor.SyncProcessor;
import com.alibaba.easyretry.common.retryer.RetryerInfo;

/**
 * @author Created by zhangchi on 2023-07-13
 */
public abstract class AbstractSyncProcessor<R> implements SyncProcessor<R> {

	protected RetryerInfo<R> retryerInfo;

	public AbstractSyncProcessor(RetryerInfo<R> retryerInfo) {
		this.retryerInfo = retryerInfo;
	}

	@Override
	public void process() {
	}
}
