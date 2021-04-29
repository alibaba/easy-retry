package com.alibaba.easyretry.core.filter;

import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.filter.RetryFilter;
import com.alibaba.easyretry.common.filter.RetryResponse;

/**
 * @author Created by wuhao on 2021/3/22.
 */
public class MethodExcuteRetryFilter implements RetryFilter {


	@Override
	public RetryResponse doFilter(RetryContext retryContext) throws Throwable {
		RetryResponse retryResponse = new RetryResponse();
		retryResponse.setResponse(retryContext.getInvocation().invoke());
		return retryResponse;
	}

	@Override
	public void setNext(RetryFilter next) {

	}
}
