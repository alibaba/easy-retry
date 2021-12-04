package com.alibaba.easyretry.core.process.async;

import com.alibaba.easyretry.common.processor.AsyncPersistenceProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Created by wuhao on 2021/3/19.
 */
@Slf4j
public abstract class AbstractAsyncPersistenceProcessor<R> implements AsyncPersistenceProcessor<R> {

	@Override
	public void process() {
		if (!needRetry()) {
			return;
		}
		doProcess();
	}

	protected abstract void doProcess();

	@Override
	public abstract boolean needRetry();

}
