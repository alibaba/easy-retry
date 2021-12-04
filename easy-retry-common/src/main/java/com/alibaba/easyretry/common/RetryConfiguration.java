package com.alibaba.easyretry.common;

import com.alibaba.easyretry.common.access.RetrySerializerAccess;
import com.alibaba.easyretry.common.access.RetryStrategyAccess;
import com.alibaba.easyretry.common.access.RetryTaskAccess;
import com.alibaba.easyretry.common.event.RetryEventMulticaster;
import com.alibaba.easyretry.common.predicate.ResultPredicateProduce;
import com.alibaba.easyretry.common.resolve.ExecutorSolver;

/**
 * @author Created by wuhao on 2020/11/5.
 */
public interface RetryConfiguration {

	RetryTaskAccess getRetryTaskAccess();

	RetrySerializerAccess getRetrySerializerAccess();

	RetryStrategyAccess getRetryStrategyAccess();

	ExecutorSolver getExecutorSolver();

	ResultPredicateProduce getResultPredicateProduce();

	Integer getMaxRetryTimes();

	RetryEventMulticaster getRetryEventMulticaster();
}
