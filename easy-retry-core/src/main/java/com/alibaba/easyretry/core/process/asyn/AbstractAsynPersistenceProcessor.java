package com.alibaba.easyretry.core.process.asyn;

import com.alibaba.easyretry.common.processor.AsynPersistenceProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Created by wuhao on 2021/3/19.
 */
@Slf4j
public abstract class AbstractAsynPersistenceProcessor<R> implements AsynPersistenceProcessor<R> {

	@Override
	public void process() {
		if (!needRetry()) {
			return;
		}
		doProcess();
	}

	protected abstract void doProcess();


	public abstract boolean needRetry();

}
