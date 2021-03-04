package com.alibaba.easyretry.common;

/**
 * @author Created by wuhao on 2020/11/2.
 */
public interface RetryLifecycle {

    void start();

    void stop();
}
