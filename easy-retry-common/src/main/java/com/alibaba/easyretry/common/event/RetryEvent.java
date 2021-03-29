package com.alibaba.easyretry.common.event;

/**
 * @author Created by wuhao on 2021/3/25.
 */
public interface RetryEvent {

	String getName();

	boolean isOnRetry();

	void setAttribute(String key, String vule);
}
