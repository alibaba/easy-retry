package com.alibaba.easyretry.core;

import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.SCallable;
import com.alibaba.easyretry.common.processor.AsyncPersistenceProcessor;
import com.alibaba.easyretry.common.retryer.Retryer;
import com.alibaba.easyretry.common.retryer.RetryerInfo;
import com.alibaba.easyretry.core.process.async.before.ExceptionPersistenceAsyncBeforeRetryProcessor;
import com.alibaba.easyretry.core.process.async.before.ResultAsynPersistenceBeforeRetryProcessor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Created by wuhao on 2020/11/1.
 */
@Slf4j
@Data
public class PersistenceRetryer<V> implements Retryer<V> {

	private RetryerInfo<V> retryerInfo;

//	private RetryConfiguration retryConfiguration;

	public PersistenceRetryer(RetryerInfo<V> retryerInfo) {
		this.retryerInfo = retryerInfo;
	}

	@Override
	public V call(SCallable<V> callable) throws Throwable {
		AsyncPersistenceProcessor<V> asynPersistenceProcessor;
		try {
			V result = callable.call();
			asynPersistenceProcessor = new ResultAsynPersistenceBeforeRetryProcessor<>(result,
				retryerInfo);
		} catch (Throwable e) {
			log.error(
				"call method error executorMethodName is {} executorName name is {} args is {}",
				retryerInfo.getExecutorMethodName(),
				retryerInfo.getExecutorName(),
				retryerInfo.getArgs(),
				e);
			asynPersistenceProcessor = new ExceptionPersistenceAsyncBeforeRetryProcessor<>(e,
				retryerInfo);
		}
		asynPersistenceProcessor.process();
		return asynPersistenceProcessor.getResult();
	}
}
