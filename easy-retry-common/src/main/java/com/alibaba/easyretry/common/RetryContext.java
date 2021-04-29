package com.alibaba.easyretry.common;

public interface RetryContext extends RetryLifecycle {

	void setAttribute(String key, String value);

	String getAttribute(String key);

	/**
	 * 获取唯一标识
	 */
	String getId();

	Invocation getInvocation();

}
