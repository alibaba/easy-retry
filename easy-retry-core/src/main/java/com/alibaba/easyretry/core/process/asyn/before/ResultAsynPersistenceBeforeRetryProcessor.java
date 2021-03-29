package com.alibaba.easyretry.core.process.asyn.before;

import com.alibaba.easyretry.common.AbstractResultPredicate;
import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.retryer.RetryerInfo;
import java.util.Objects;

/**
 * @author Created by wuhao on 2021/3/19.
 */
public class ResultAsynPersistenceBeforeRetryProcessor<R> extends
	AbstractAsynPersistenceBeforeRetryProcessor<R> {

	private R result;

	public ResultAsynPersistenceBeforeRetryProcessor(R result, RetryerInfo retryerInfo,
		RetryConfiguration retryConfiguration) {
		super(retryerInfo, retryConfiguration);
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
