package com.alibaba.easyretry.common.event;

/**
 * @author Created by wuhao on 2021/3/25.
 */
public interface RetryListener<T extends RetryEvent> {

	void onRetryEvent(T event);

}
