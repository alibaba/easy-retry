package com.alibaba.easyretry.common.predicate;

public interface ResultPredicateProduce {

	<T> AbstractResultPredicate<T> produce();

}
