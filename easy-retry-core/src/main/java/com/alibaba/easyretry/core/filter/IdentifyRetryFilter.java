package com.alibaba.easyretry.core.filter;

import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.RetryIdentify;
import com.alibaba.easyretry.common.filter.RetryFilter;
import com.alibaba.easyretry.common.filter.RetryFilterChain;
import com.alibaba.easyretry.common.filter.RetryFilterResponse;

/**
 * @author Created by wuhao on 2021/3/22.
 */
public class IdentifyRetryFilter implements RetryFilter {
	@Override
	public RetryFilterResponse doFilter(RetryFilterChain invocation, RetryContext retryContext) throws Throwable {
		try {
			RetryIdentify.start();
			return invocation.invoke(retryContext);
		} finally {
			RetryIdentify.stop();
		}
	}
}
