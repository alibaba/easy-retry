package com.alibaba.easyretry.common.filter;

import com.alibaba.easyretry.common.RetryContext;

/**
 * @author Created by wuhao on 2021/4/10.
 */
public interface RetryFilterChain {

	RetryFilterResponse invoke(RetryContext retryContext) throws Throwable;

}
