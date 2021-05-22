package com.alibaba.easyretry.core.process.async.before;

import java.util.Objects;

import com.alibaba.easyretry.common.AbstractResultPredicate;
import com.alibaba.easyretry.common.retryer.RetryerInfo;

/**
 * @author Created by wuhao on 2021/3/19.
 */
public class ResultAsynPersistenceBeforeRetryProcessor<R> extends
    AbstractAsyncPersistenceBeforeRetryProcessor<R> {

	private final R result;

	public ResultAsynPersistenceBeforeRetryProcessor(R result, RetryerInfo<R> retryerInfo) {
		super(retryerInfo);
		this.result = result;
	}

	@Override
	public boolean needRetry() {
		AbstractResultPredicate<R> easyRetryPredicate = retryerInfo.getResultPredicate();
		if (Objects.nonNull(easyRetryPredicate)) {
			return easyRetryPredicate.apply(result);
		} else {
			return false;
		}
	}

	@Override
	public R getResult() {
		return result;
	}
}
