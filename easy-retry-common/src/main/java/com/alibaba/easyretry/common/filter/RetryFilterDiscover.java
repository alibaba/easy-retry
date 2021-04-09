package com.alibaba.easyretry.common.filter;

import java.util.List;

import com.alibaba.easyretry.common.filter.RetryFilter;

/**
 * @author Created by wuhao on 2021/4/9.
 */
public interface RetryFilterDiscover {

	List<RetryFilter> discoverAll();
}
