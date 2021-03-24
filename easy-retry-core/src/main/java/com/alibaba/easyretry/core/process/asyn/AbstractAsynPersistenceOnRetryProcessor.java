package com.alibaba.easyretry.core.process.asyn;

import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.constant.enums.HandleResultEnum;
import com.alibaba.easyretry.core.utils.PrintUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Created by wuhao on 2021/3/19.
 */
@Slf4j
public abstract class AbstractAsynPersistenceOnRetryProcessor<R> extends
	AbstractAsynPersistenceProcessor<R> {

	protected RetryContext context;

	private HandleResultEnum retryResult;


	public AbstractAsynPersistenceOnRetryProcessor(RetryContext context) {
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
		String message = PrintUtils.printCommonMethodInfo(context);
		if (context.getStopStrategy().shouldStop(context)) {
			log.error(message + "will stop");
			retryResult = HandleResultEnum.STOP;
		} else {
			log.error(message + "will try later");
			context.getWaitStrategy().backOff(context);
			retryResult = HandleResultEnum.FAILURE;
		}
	}

	public HandleResultEnum getRetryResult() {
		return retryResult;
	}

}
