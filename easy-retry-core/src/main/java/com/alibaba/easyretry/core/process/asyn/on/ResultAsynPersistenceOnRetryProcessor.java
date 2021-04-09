package com.alibaba.easyretry.core.process.asyn.on;

import com.alibaba.easyretry.common.AbstractResultPredicate;
import com.alibaba.easyretry.core.context.MaxAttemptsPersistenceRetryContext;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Created by wuhao on 2021/3/19.
 */
public class ResultAsynPersistenceOnRetryProcessor<R> extends
	AbstractAsynPersistenceOnRetryProcessor<R> {

	private R result;

	public ResultAsynPersistenceOnRetryProcessor(R result,
												 MaxAttemptsPersistenceRetryContext context) {
		super(context);
		this.result = result;
	}

	@Override
	public boolean needRetry() {
		String resultPredicateSerializerStr = context.getAttribute("resultPredicateSerializer");
		if (StringUtils.isBlank(resultPredicateSerializerStr)) {
			return false;
		}
		AbstractResultPredicate resultPredicate = context.getResultPredicateSerializer()
			.deSerialize(resultPredicateSerializerStr);
		return resultPredicate.apply(result);
	}

	@Override
	public R getResult() {
		return result;
	}
}
