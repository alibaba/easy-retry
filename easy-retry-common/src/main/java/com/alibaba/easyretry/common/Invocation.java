package com.alibaba.easyretry.common;

import java.lang.reflect.Method;

/**
 * @author Created by wuhao on 2021/3/29.
 */
public interface Invocation {

	Object invokeRetryMethod() throws Throwable;

	Object invokeMethod(Method method, Object[] args) throws Throwable;

}
