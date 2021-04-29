package com.alibaba.easyretry.common.event;

/**
 * @author Created by wuhao on 2021/3/26.
 */
public interface RetryEventMulticaster {

	void register(RetryListener listener);

	void multicast(RetryEvent retryEvent);

}
