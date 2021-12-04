package com.alibaba.easyretry.common;

import java.lang.reflect.Method;

import lombok.Setter;

/**
 * @author Created by wuhao on 2021/3/29.
 */
public interface InvokeExecutor {

	Object invokeRetryMethod() throws Throwable;

}
