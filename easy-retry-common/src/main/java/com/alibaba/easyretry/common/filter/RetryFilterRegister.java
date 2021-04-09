package com.alibaba.easyretry.common.filter;

import java.util.List;

/**
 * @author Created by wuhao on 2021/3/22.
 */
public interface RetryFilterRegister {

	void register(RetryFilter retryFilter);

	List<RetryFilter> export();

}
