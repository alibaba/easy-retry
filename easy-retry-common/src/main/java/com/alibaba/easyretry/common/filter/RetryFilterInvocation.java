package com.alibaba.easyretry.common.filter;

import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.filter.RetryFilter;
import com.alibaba.easyretry.common.filter.RetryFilterResponse;

/**
 * @author Created by wuhao on 2021/4/10.
 */
public interface RetryFilterInvocation {

	RetryFilterResponse invoke(RetryContext retryContext) throws Throwable;

}
