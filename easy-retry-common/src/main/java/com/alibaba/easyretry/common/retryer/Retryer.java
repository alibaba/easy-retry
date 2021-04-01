package com.alibaba.easyretry.common.retryer;

import com.alibaba.easyretry.common.SCallable;

/**
 * @author Created by wuhao on 2020/11/5.
 */
public interface Retryer<V> {

	 V call(SCallable<V> callable) throws Throwable;

}
