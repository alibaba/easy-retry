package com.alibaba.easyretry.core.process.asyn;

import com.alibaba.easyretry.common.ResultPredicate;
import com.alibaba.easyretry.common.retryer.RetryerInfo;
import java.util.Objects;

/**
 * @author Created by wuhao on 2021/3/19.
 */
public class ResultAsynPersistenceBeforeRetryProcessor<R> extends
	AbstractAsynPersistenceBeforeRetryProcessor<R> {

	private R result;

	public ResultAsynPersistenceBeforeRetryProcessor(R result,
		RetryerInfo retryerInfo) {
		super(retryerInfo);
		this.result = result;
	}

	@Override
	public boolean needRetry() {
		ResultPredicate<R> resultPredicate = retryerInfo.getResultPredicate();
		if (Objects.nonNull(resultPredicate)) {
			return resultPredicate.apply(result);
		} else {
			return false;
		}
	}

	@Override
	public R getResult() {
		return result;
	}
}
