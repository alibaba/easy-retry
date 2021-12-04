package com.alibaba.easyretry.core.process.async.before;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.easyretry.common.predicate.AbstractResultPredicate;
import com.alibaba.easyretry.common.retryer.RetryerInfo;
import com.alibaba.easyretry.core.process.async.before.AbstractAsyncPersistenceBeforeRetryProcessor;

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
		String resultCondition = retryerInfo.getResultCondition();
		if (StringUtils.isNotBlank(resultCondition)) {
			AbstractResultPredicate<R> abstractResultPredicate = retryerInfo.getRetryConfiguration().getResultPredicateProduce().produce();
			return abstractResultPredicate.apply(result);
		} else {
			return false;
		}
	}

	@Override
	public R getResult() {
		return result;
	}
}
