package com.alibaba.easyretry.common.filter;

import com.alibaba.easyretry.common.RetryContext;

/**
 * @author Created by wuhao on 2021/3/19.
 */
public interface RetryInvocationHandler {

	RetryResponse invoke(RetryContext retryContext) throws Throwable;


}
