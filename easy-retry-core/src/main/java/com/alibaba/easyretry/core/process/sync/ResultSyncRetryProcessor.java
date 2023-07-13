package com.alibaba.easyretry.core.process.sync;

import com.alibaba.easyretry.common.retryer.RetryerInfo;

/**
 * @author Created by zhangchi on 2023-07-13
 */
public class ResultSyncRetryProcessor<R> extends AbstractSyncProcessor<R> {

	private final R result;

	public ResultSyncRetryProcessor(R result, RetryerInfo<R> retryerInfo) {
		super(retryerInfo);
		this.result = result;
	}

	@Override
	public R getResult() {
		return result;
	}

}
