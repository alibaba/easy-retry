package com.alibaba.easyretry.common;

/**
 * @author Created by wuhao on 2021/3/29.
 */
public interface Invocation {

	Object invoke() throws Throwable;

}
