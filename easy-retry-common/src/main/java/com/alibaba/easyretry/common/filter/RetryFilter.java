package com.alibaba.easyretry.common.filter;

import com.alibaba.easyretry.common.RetryContext;

/**
 * @author Created by wuhao on 2021/3/22.
 */
public interface RetryFilter {

	RetryResponse doFilter(RetryContext retryContext) throws Throwable;

	void setNext(RetryFilter next);

}
