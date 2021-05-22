package com.alibaba.easyretry.extension.spring;

import java.util.List;
import java.util.Map;

import com.alibaba.easyretry.common.filter.RetryFilter;
import com.alibaba.easyretry.common.filter.RetryFilterDiscover;

import com.google.common.collect.Lists;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Created by wuhao on 2021/4/9.
 */
public class SpringRetryFilterDiscover implements RetryFilterDiscover, SmartInitializingSingleton,
	ApplicationContextAware {

	private List<RetryFilter> retryFilters;

	private ApplicationContext applicationContext;

	@Override
	public List<RetryFilter> discoverAll() {
		return retryFilters;
	}

	@Override
	public void afterSingletonsInstantiated() {
		Map<String, RetryFilter> retryFilterMap = applicationContext.getBeansOfType(RetryFilter.class);
		retryFilters = Lists.newArrayList(retryFilterMap.values());
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
