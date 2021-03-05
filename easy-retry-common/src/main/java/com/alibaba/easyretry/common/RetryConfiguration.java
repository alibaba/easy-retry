package com.alibaba.easyretry.common;

import com.alibaba.easyretry.common.access.RetrySerializerAccess;
import com.alibaba.easyretry.common.access.RetryStrategyAccess;
import com.alibaba.easyretry.common.access.RetryTaskAccess;
import com.alibaba.easyretry.common.resolve.ExecutorSolver;

/**
 * @author Created by wuhao on 2020/11/5.
 */
public interface RetryConfiguration {

    RetryTaskAccess getRetryTaskAccess();

    RetrySerializerAccess getRetrySerializerAccess();

    RetryStrategyAccess getRetryStrategyAccess();

    ExecutorSolver getExecutorSolver();

    Integer getMaxRetryTimes();

}
