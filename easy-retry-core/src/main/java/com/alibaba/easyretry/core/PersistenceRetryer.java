package com.alibaba.easyretry.core;

import com.alibaba.easyretry.common.SCallable;
import com.alibaba.easyretry.common.processor.AsynPersistenceProcessor;
import com.alibaba.easyretry.common.retryer.Retryer;
import com.alibaba.easyretry.common.retryer.RetryerInfo;
import com.alibaba.easyretry.core.process.asyn.ExceptionPersistenceAsynBeforeRetryProcessor;
import com.alibaba.easyretry.core.process.asyn.ResultAsynPersistenceBeforeRetryProcessor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Created by wuhao on 2020/11/1.
 */
@Slf4j
@Data
public class PersistenceRetryer implements Retryer {

	private RetryerInfo retryerInfo;

	public PersistenceRetryer(RetryerInfo retryerInfo) {
		this.retryerInfo = retryerInfo;
	}

	public <V> V call(SCallable<V> callable) throws Throwable {
		AsynPersistenceProcessor<V> asynPersistenceProcessor;
		try {
			V result = callable.call();
			asynPersistenceProcessor = new ResultAsynPersistenceBeforeRetryProcessor<>(result,retryerInfo);
		} catch (Throwable e) {
			log.error(
				"call method error executorMethodName is {} executorName name is {} args is {}",
				retryerInfo.getExecutorMethodName(),
				retryerInfo.getExecutorName(),
				retryerInfo.getArgs(),
				e);
			asynPersistenceProcessor = new ExceptionPersistenceAsynBeforeRetryProcessor<>(e,retryerInfo);
		}
		asynPersistenceProcessor.process();
		return asynPersistenceProcessor.getResult();
	}
}
