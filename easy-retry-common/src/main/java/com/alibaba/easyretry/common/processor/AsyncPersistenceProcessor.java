package com.alibaba.easyretry.common.processor;

/**
 * @author Created by wuhao on 2021/3/19.
 */
public interface AsyncPersistenceProcessor<R> extends RetryProcessor {

    R getResult() throws Throwable;

}
