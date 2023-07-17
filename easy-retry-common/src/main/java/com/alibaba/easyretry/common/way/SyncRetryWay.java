package com.alibaba.easyretry.common.way;

import com.alibaba.easyretry.common.SCallable;
import com.alibaba.easyretry.common.retryer.RetryerInfo;

/**
 * @author zhangchi20 <zhangchi20@kuaishou.com>
 * Created on 2023-07-17
 */
public interface SyncRetryWay<V> {

	V call(SCallable<V> callable, RetryerInfo<V> retryerInfo) throws Throwable;
}
