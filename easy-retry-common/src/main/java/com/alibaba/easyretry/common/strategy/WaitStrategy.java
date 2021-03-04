package com.alibaba.easyretry.common.strategy;

import com.alibaba.easyretry.common.RetryContext;

/**
 * @author Created by wuhao on 2020/11/1.
 */
public interface WaitStrategy extends RetryStrategy {

    boolean shouldWait(RetryContext context);

    void backOff(RetryContext context);

}
