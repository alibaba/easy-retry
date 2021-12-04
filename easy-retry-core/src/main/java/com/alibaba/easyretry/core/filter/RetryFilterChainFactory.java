package com.alibaba.easyretry.core.filter;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.alibaba.easyretry.common.InvokeExecutor;
import com.alibaba.easyretry.common.filter.RetryFilter;
import com.alibaba.easyretry.common.filter.RetryFilterChain;
import com.alibaba.easyretry.common.filter.RetryFilterRegister;
import com.google.common.collect.Lists;

import lombok.Setter;

public class RetryFilterChainFactory {

	@Setter
	private RetryFilterRegister retryFilterRegister;

	public RetryFilterChain createFilterChain(InvokeExecutor invokeExecutor) {
		List<RetryFilter> filters = Lists.newArrayList();
		filters.add(new IdentifyRetryFilter());
		List<RetryFilter> exportFilters = retryFilterRegister.export();
		if (CollectionUtils.isNotEmpty(exportFilters)) {
			filters.addAll(exportFilters);
		}

		DefaultRetryFilterChain defaultRetryFilterChain = new DefaultRetryFilterChain();
		defaultRetryFilterChain.setFilters(filters);
		defaultRetryFilterChain.setSize(filters.size());
		defaultRetryFilterChain.setInvokeExecutor(invokeExecutor);
		return defaultRetryFilterChain;
	}
}
