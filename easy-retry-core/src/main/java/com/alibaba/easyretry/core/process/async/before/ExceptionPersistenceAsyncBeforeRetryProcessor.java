package com.alibaba.easyretry.core.process.async.before;

import java.util.Objects;

import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.retryer.RetryerInfo;

import org.apache.commons.lang3.ClassUtils;

/**
 * @author Created by wuhao on 2021/3/19.
 */
public class ExceptionPersistenceAsyncBeforeRetryProcessor<R> extends
	AbstractAsyncPersistenceBeforeRetryProcessor<R> {

	private Throwable throwable;

	public ExceptionPersistenceAsyncBeforeRetryProcessor(Throwable throwable,
		RetryerInfo<R> retryerInfo) {
		super(retryerInfo);
		this.throwable = throwable;
	}

	@Override
	public boolean needRetry() {
		Class<? extends Throwable> onException = retryerInfo.getOnException();
		if (Objects.isNull(onException)) {
			return true;
		}
		return ClassUtils.isAssignable(retryerInfo.getOnException(), throwable.getClass());
	}

	@Override
	public R getResult() throws Throwable {
		if (retryerInfo.isReThrowException()) {
			throw throwable;
		} else {
			return null;
		}
	}
}
