package com.alibaba.easyretry.core.process.sync;

import com.alibaba.easyretry.common.retryer.RetryerInfo;

/**
 * @author Created by zhangchi on 2023-07-13
 */
public class ExceptionSyncRetryProcessorr<R> extends AbstractSyncProcessor<R> {

	private Throwable throwable;

	public ExceptionSyncRetryProcessorr(Throwable throwable, RetryerInfo<R> retryerInfo) {
		super(retryerInfo);
		this.throwable = throwable;
	}

	@Override
	public R getResult() throws Throwable {
		throw throwable;
	}
}
