package com.alibaba.easyretry.common.strategy;

import com.alibaba.easyretry.common.RetryContext;

/**
 * @author Created by wuhao on 2020/11/2.
 */
public interface RetryStrategy {

    void clear(RetryContext context);

}
