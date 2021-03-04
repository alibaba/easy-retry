package com.alibaba.easyretry.common;

import java.io.Serializable;

/**
 * @author Created by wuhao on 2020/11/5.
 */
@FunctionalInterface
public interface SCallable<V> extends  Serializable {

    V call() throws Throwable;

}
