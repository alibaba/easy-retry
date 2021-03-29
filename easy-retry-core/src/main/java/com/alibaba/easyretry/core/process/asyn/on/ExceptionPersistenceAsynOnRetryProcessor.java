package com.alibaba.easyretry.core.process.asyn.on;

import com.alibaba.easyretry.core.context.MaxAttemptsPersistenceRetryContext;

/**
 * @author Created by wuhao on 2021/3/19.
 */
public class ExceptionPersistenceAsynOnRetryProcessor<R> extends
	AbstractAsynPersistenceOnRetryProcessor<R> {

	private Throwable throwable;

	public ExceptionPersistenceAsynOnRetryProcessor(Throwable throwable,
		MaxAttemptsPersistenceRetryContext context) {
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
