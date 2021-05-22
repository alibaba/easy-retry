package com.alibaba.easyretry.core.filter;

import java.util.List;

import com.alibaba.easyretry.common.filter.RetryFilter;
import com.alibaba.easyretry.common.filter.RetryFilterRegister;

import com.google.common.collect.Lists;

/**
 * @author Created by wuhao on 2021/4/9.
 */
public class SimpleRetryFilterRegister implements RetryFilterRegister {

	private List<RetryFilter> retryFiltersCache = Lists.newArrayList();

	@Override
	public void register(RetryFilter retryFilter) {
		retryFiltersCache.add(retryFilter);
	}

	@Override
	public List<RetryFilter> export() {
		return retryFiltersCache;
	}
}
