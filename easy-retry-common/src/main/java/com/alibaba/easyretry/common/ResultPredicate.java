package com.alibaba.easyretry.common;

import java.io.Serializable;

/**
 * @author Created by wuhao on 2021/3/18.
 */
public interface ResultPredicate<R> extends Serializable {

	boolean apply(R var1);

}
