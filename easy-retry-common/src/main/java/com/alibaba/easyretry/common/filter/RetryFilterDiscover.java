package com.alibaba.easyretry.common.filter;

import java.util.List;

/**
 * @author Created by wuhao on 2021/4/9.
 */
public interface RetryFilterDiscover {

	List<RetryFilter> discoverAll();
}
