package com.alibaba.easyretry.common.predicate;

import java.io.Serializable;

/**
 * @author Created by wuhao on 2021/3/18.
 */
public interface EasyRetryPredicate<T, R> extends Serializable {

	R apply(T result);

}
