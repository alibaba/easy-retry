package com.alibaba.easyretry.extension.spring;

import com.alibaba.easyretry.common.predicate.AbstractResultPredicate;
import com.alibaba.easyretry.common.predicate.ResultPredicateProduce;

public class SPELResultPredicateProduce implements ResultPredicateProduce {

	@Override
	public <T> AbstractResultPredicate<T> produce() {
		return new SPELResultPredicate<>();
	}

}
