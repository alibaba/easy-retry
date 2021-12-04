package com.alibaba.easyretry.common.predicate;

/**
 * @author Created by wuhao on 2021/3/26.
 */
public abstract class AbstractResultPredicate<T> implements EasyRetryPredicate<T, Boolean> {

	@Override
	public abstract Boolean apply(T result);

}
