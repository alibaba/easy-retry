package com.alibaba.easyretry.core.filter;

import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.filter.RetryFilter;
import com.alibaba.easyretry.common.filter.RetryFilterResponse;

/**
 * @author Created by wuhao on 2021/3/22.
 */
public class MethodExcuteRetryFilter implements RetryFilter {

	@Override
	public RetryFilterResponse doFilter(RetryContext retryContext) throws Throwable {
		RetryFilterResponse retryFilterResponse = new RetryFilterResponse();
		retryFilterResponse.setResponse(retryContext.getInvocation().invoke());
		return retryFilterResponse;
	}

	@Override
	public void setNext(RetryFilter next) {

	}
}
