package com.alibaba.easyretry.core.process.asyn.on;

import com.alibaba.easyretry.common.constant.enums.HandleResultEnum;
import com.alibaba.easyretry.core.context.MaxAttemptsPersistenceRetryContext;
import com.alibaba.easyretry.core.process.asyn.AbstractAsynPersistenceProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Created by wuhao on 2021/3/19.
 */
@Slf4j
public abstract class AbstractAsynPersistenceOnRetryProcessor<R> extends
	AbstractAsynPersistenceProcessor<R> {

	protected MaxAttemptsPersistenceRetryContext context;

	private HandleResultEnum retryResult;


	public AbstractAsynPersistenceOnRetryProcessor(MaxAttemptsPersistenceRetryContext context) {
		this.context = context;
	}

	@Override
	public void process() {
		if (!needRetry()) {
			retryResult = HandleResultEnum.SUCCESS;
		}
		doProcess();
	}

	@Override
	public void doProcess() {
		if (context.getStopStrategy().shouldStop(context)) {
			log.error(context.getInvocation() + " will stop");
			retryResult = HandleResultEnum.STOP;
		} else {
			log.error(context.getInvocation() + " will try later");
			context.getWaitStrategy().backOff(context);
			retryResult = HandleResultEnum.FAILURE;
		}
	}

	public HandleResultEnum getRetryResult() {
		return retryResult;
	}

}
