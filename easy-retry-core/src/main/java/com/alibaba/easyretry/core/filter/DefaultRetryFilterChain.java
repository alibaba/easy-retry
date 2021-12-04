package com.alibaba.easyretry.core.filter;

import java.util.List;

import com.alibaba.easyretry.common.InvokeExecutor;
import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.filter.RetryFilter;
import com.alibaba.easyretry.common.filter.RetryFilterChain;
import com.alibaba.easyretry.common.filter.RetryFilterResponse;

import lombok.Setter;

/**
 * @author Created by wuhao on 2021/3/22.
 */
public class DefaultRetryFilterChain implements RetryFilterChain {

	@Setter
	private List<RetryFilter> filters;

	@Setter
	private InvokeExecutor invokeExecutor;

	private int pos = 0;

	@Setter
	private int size = 0;

	@Override
	public RetryFilterResponse invoke(RetryContext retryContext) throws Throwable {
		if (pos < size) {
			filters.get(pos++).doFilter(this, retryContext);
		}
		Object object = invokeExecutor.invokeRetryMethod();
		RetryFilterResponse retryFilterResponse = new RetryFilterResponse();
		retryFilterResponse.setResponse(object);
		return retryFilterResponse;
	}
}
