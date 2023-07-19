package com.alibaba.easyretry.common;

import com.alibaba.easyretry.common.retryer.RetryerInfo;

/**
 * @author zhangchi20
 * Created on 2023-07-17
 */
public interface RetrySyncExecutor<V> {

	V call(SCallable<V> callable) throws Throwable;

	void setRetryerInfo(RetryerInfo<V> retryerInfo);

}
