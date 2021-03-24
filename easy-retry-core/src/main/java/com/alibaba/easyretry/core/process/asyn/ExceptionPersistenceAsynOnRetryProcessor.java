package com.alibaba.easyretry.core.process.asyn;

import com.alibaba.easyretry.common.RetryContext;

/**
 * @author Created by wuhao on 2021/3/19.
 */
public class ExceptionPersistenceAsynOnRetryProcessor<R> extends
	AbstractAsynPersistenceOnRetryProcessor {

	private Throwable throwable;

	public ExceptionPersistenceAsynOnRetryProcessor(Throwable throwable, RetryContext context) {
		super(context);
		this.throwable = throwable;
	}

	@Override
	public boolean needRetry() {
		return true;
	}

	@Override
	public R getResult() {
		return null;
	}
}
