package com.alibaba.easyretry.core.filter;

import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.filter.RetryFilter;
import com.alibaba.easyretry.common.filter.RetryInvocationHandler;
import com.alibaba.easyretry.common.filter.RetryResponse;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author Created by wuhao on 2021/3/22.
 */
public class DefaultRetryInvocationHandler implements RetryInvocationHandler {

	private RetryFilter firstFilter;

	public void init() {
		ServiceLoader<RetryFilter> retryFilters = ServiceLoader.load(RetryFilter.class);
		Iterator<RetryFilter> iterator = retryFilters.iterator();

		firstFilter = new NOOPRetryFilter();
		RetryFilter lastRetryFilter = firstFilter;
		while (iterator.hasNext()) {
			RetryFilter retryFilter = iterator.next();
			lastRetryFilter.setNext(retryFilter);
			lastRetryFilter = retryFilter;
		}

		IdentifyRetryFilter identifyRetryFilter = new IdentifyRetryFilter();
		lastRetryFilter.setNext(identifyRetryFilter);
		lastRetryFilter = identifyRetryFilter;

		MethodExcuteRetryFilter methodExcuteRetryFilter = new MethodExcuteRetryFilter();
		lastRetryFilter.setNext(methodExcuteRetryFilter);
	}

	@Override
	public RetryResponse invoke(RetryContext retryContext) throws Throwable {
		return firstFilter.doFilter(retryContext);
	}
}
