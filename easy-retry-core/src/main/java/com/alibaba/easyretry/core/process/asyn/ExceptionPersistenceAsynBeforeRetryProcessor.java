package com.alibaba.easyretry.core.process.asyn;

import com.alibaba.easyretry.common.retryer.RetryerInfo;
import java.util.Objects;
import org.apache.commons.lang3.ClassUtils;

/**
 * @author Created by wuhao on 2021/3/19.
 */
public class ExceptionPersistenceAsynBeforeRetryProcessor<R> extends
	AbstractAsynPersistenceBeforeRetryProcessor {

	private Throwable throwable;

	public ExceptionPersistenceAsynBeforeRetryProcessor(Throwable throwable,
		RetryerInfo retryerInfo) {
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
