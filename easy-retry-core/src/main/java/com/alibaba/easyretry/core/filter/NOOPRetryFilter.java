package com.alibaba.easyretry.core.filter;

import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.filter.AbstractRetryFilter;
import com.alibaba.easyretry.common.filter.RetryResponse;

/**
 * @author Created by wuhao on 2021/3/22.
 */
public class NOOPRetryFilter extends AbstractRetryFilter {

	@Override
	public RetryResponse doFilter(RetryContext retryContext) throws Throwable {
		return next.doFilter(retryContext);
	}

}
