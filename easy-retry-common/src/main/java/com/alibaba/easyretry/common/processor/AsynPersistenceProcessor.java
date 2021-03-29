package com.alibaba.easyretry.common.processor;

/**
 * @author Created by wuhao on 2021/3/19.
 */
public interface AsynPersistenceProcessor<R> extends RetryProcessor {

	void process();

	boolean needRetry();

	R getResult() throws Throwable;

}
