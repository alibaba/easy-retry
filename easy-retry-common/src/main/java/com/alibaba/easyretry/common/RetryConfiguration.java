package com.alibaba.easyretry.common;

import com.alibaba.easyretry.common.access.RetrySerializerAccess;
import com.alibaba.easyretry.common.access.RetryStrategyAccess;
import com.alibaba.easyretry.common.access.RetryTaskAccess;
import com.alibaba.easyretry.common.event.RetryEventMulticaster;
import com.alibaba.easyretry.common.resolve.ExecutorSolver;
import com.alibaba.easyretry.common.serializer.ResultPredicateSerializer;

/**
 * @author Created by wuhao on 2020/11/5.
 */
public interface RetryConfiguration {

	RetryTaskAccess getRetryTaskAccess();

	RetrySerializerAccess getRetrySerializerAccess();

	RetryStrategyAccess getRetryStrategyAccess();

	ExecutorSolver getExecutorSolver();

	ResultPredicateSerializer getResultPredicateSerializer();

	Integer getMaxRetryTimes();

	RetryEventMulticaster getRetryEventMulticaster();
}
