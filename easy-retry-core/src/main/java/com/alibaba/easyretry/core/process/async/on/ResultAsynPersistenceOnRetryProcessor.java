package com.alibaba.easyretry.core.process.async.on;

import com.alibaba.easyretry.common.predicate.AbstractResultPredicate;
import com.alibaba.easyretry.core.context.MaxAttemptsPersistenceRetryContext;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Created by wuhao on 2021/3/19.
 */
public class ResultAsynPersistenceOnRetryProcessor<R> extends
    AbstractAsyncPersistenceOnRetryProcessor<R> {

	private R result;

	public ResultAsynPersistenceOnRetryProcessor(R result,
												 MaxAttemptsPersistenceRetryContext context) {
		super(context);
		this.result = result;
	}

	@Override
	public boolean needRetry() {
		//TODO
//		String resultPredicateSerializerStr = context.getAttribute("resultPredicateSerializer");
//		if (StringUtils.isBlank(resultPredicateSerializerStr)) {
//			return false;
//		}
//		AbstractResultPredicate resultPredicate = context.getResultPredicateSerializer()
//			.deSerialize(resultPredicateSerializerStr);
//		return resultPredicate.apply(result);
//
//		String resultCondition = retryerInfo.getResultCondition();
//		if (StringUtils.isNotBlank(resultCondition)) {
//			AbstractResultPredicate<R> abstractResultPredicate = retryerInfo.getRetryConfiguration().getResultPredicateProduce().produce();
//			return abstractResultPredicate.apply(result);
//		} else {
			return false;
//		}
	}

	@Override
	public R getResult() {
		return result;
	}
}
