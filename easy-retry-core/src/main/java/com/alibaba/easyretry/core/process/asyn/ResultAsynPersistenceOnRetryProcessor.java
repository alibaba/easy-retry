package com.alibaba.easyretry.core.process.asyn;

import com.alibaba.easyretry.common.ResultPredicate;
import com.alibaba.easyretry.common.RetryContext;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Created by wuhao on 2021/3/19.
 */
public class ResultAsynPersistenceOnRetryProcessor<R> extends
	AbstractAsynPersistenceOnRetryProcessor<R> {

	private R result;

	public ResultAsynPersistenceOnRetryProcessor(R result, RetryContext context) {
		super(context);
		this.result = result;
	}

	@Override
	public boolean needRetry() {
		Map<String, String> extAttrs = context.getRetryTask().getExtAttrs();
		if (Objects.isNull(extAttrs)) {
			return false;
		}
		String resultPredicateSerializerStr = extAttrs.get("resultPredicateSerializer");
		if (StringUtils.isBlank(resultPredicateSerializerStr)) {
			return false;
		}
		ResultPredicate resultPredicate = context.getResultPredicateSerializer()
			.deSerialize(resultPredicateSerializerStr);
		return resultPredicate.apply(result);
	}

	@Override
	public R getResult() {
		return result;
	}
}
