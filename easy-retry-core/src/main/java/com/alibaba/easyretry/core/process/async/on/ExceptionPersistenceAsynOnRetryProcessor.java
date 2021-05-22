package com.alibaba.easyretry.core.process.async.on;

import com.alibaba.easyretry.core.context.MaxAttemptsPersistenceRetryContext;

/**
 * @author Created by wuhao on 2021/3/19.
 */
public class ExceptionPersistenceAsynOnRetryProcessor<R> extends
	AbstractAsyncPersistenceOnRetryProcessor<R> {

	private final Throwable throwable;

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
