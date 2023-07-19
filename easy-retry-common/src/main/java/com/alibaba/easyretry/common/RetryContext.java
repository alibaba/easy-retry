package com.alibaba.easyretry.common;

public interface RetryContext extends RetryLifecycle {

	void setAttribute(String key, String value);

	String getAttribute(String key);

	String getId();

	Invocation getInvocation();

}
