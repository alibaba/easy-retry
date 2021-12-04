package com.alibaba.easyretry.common;

/**
 * @author Created by wuhao on 2021/3/26.
 */
public abstract class AbstractResultPredicate<T> implements EasyRetryPredicate<T, Boolean> {

	@Override
	public abstract Boolean apply(T result);

}
