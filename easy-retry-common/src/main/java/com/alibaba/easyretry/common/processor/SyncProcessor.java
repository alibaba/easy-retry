package com.alibaba.easyretry.common.processor;

/**
 * @author Created by zhangchi on 2023-07-12
 */
public interface SyncProcessor<R> extends RetryProcessor {

	R getResult() throws Throwable;

}
