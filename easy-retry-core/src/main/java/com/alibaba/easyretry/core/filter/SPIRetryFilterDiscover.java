package com.alibaba.easyretry.core.filter;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import com.alibaba.easyretry.common.filter.RetryFilter;
import com.alibaba.easyretry.common.filter.RetryFilterDiscover;

import com.google.common.collect.Lists;

/**
 * @author Created by wuhao on 2021/4/9.
 */
public class SPIRetryFilterDiscover implements RetryFilterDiscover {

	@Override
	public List<RetryFilter> discoverAll() {
		ServiceLoader<RetryFilter> retryFilters = ServiceLoader.load(RetryFilter.class);
		Iterator<RetryFilter> iterator = retryFilters.iterator();
		return Lists.newArrayList(iterator);
	}
}
