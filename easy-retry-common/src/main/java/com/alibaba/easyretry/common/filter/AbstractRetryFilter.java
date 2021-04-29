package com.alibaba.easyretry.common.filter;

/**
 * @author Created by wuhao on 2021/3/22.
 */
public abstract class AbstractRetryFilter implements RetryFilter {

	protected RetryFilter next;


	@Override
	public void setNext(RetryFilter next) {
		this.next = next;
	}
}
