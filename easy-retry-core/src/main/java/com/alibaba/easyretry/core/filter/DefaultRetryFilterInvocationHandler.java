package com.alibaba.easyretry.core.filter;

import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.filter.RetryFilter;
import com.alibaba.easyretry.common.filter.RetryFilterInvocation;
import com.alibaba.easyretry.common.filter.RetryFilterRegister;
import com.alibaba.easyretry.common.filter.RetryFilterInvocationHandler;
import com.alibaba.easyretry.common.filter.RetryFilterResponse;

import java.util.List;

import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @author Created by wuhao on 2021/3/22.
 */
public class DefaultRetryFilterInvocationHandler implements RetryFilterInvocationHandler, RetryFilterInvocation {

	private RetryFilter firstFilter;

	@Setter
	private RetryFilterRegister retryFilterRegister;

	@Override
	public RetryFilterResponse invoke(RetryContext retryContext) throws Throwable {
		return firstFilter.doFilter(retryContext);
	}

	@Override
	public void handle() {
		List<RetryFilter> retryFilters =  retryFilterRegister.export();
		firstFilter = new NOOPRetryFilter();
		RetryFilter lastRetryFilter = firstFilter;

		for (RetryFilter retryFilter : CollectionUtils.emptyIfNull(retryFilters)) {
			lastRetryFilter.setNext(retryFilter);
			lastRetryFilter = retryFilter;
		}

		IdentifyRetryFilter identifyRetryFilter = new IdentifyRetryFilter();
		lastRetryFilter.setNext(identifyRetryFilter);
		lastRetryFilter = identifyRetryFilter;

		MethodExcuteRetryFilter methodExcuteRetryFilter = new MethodExcuteRetryFilter();
		lastRetryFilter.setNext(methodExcuteRetryFilter);

	}
}
