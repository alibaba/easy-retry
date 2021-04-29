package com.alibaba.easyretry.core.process.async.before;

import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.constant.enums.RetryTaskStatusEnum;
import com.alibaba.easyretry.common.entity.RetryTask;
import com.alibaba.easyretry.common.event.before.AfterSaveBeforeRetryEvent;
import com.alibaba.easyretry.common.event.before.PrepSaveBeforeRetryEvent;
import com.alibaba.easyretry.common.retryer.RetryerInfo;
import com.alibaba.easyretry.common.serializer.ArgSerializerInfo;
import com.alibaba.easyretry.core.process.async.AbstractAsyncPersistenceProcessor;
import com.google.common.collect.Maps;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Created by wuhao on 2021/3/19.
 */
@Slf4j
public abstract class AbstractAsyncPersistenceBeforeRetryProcessor<R> extends
	AbstractAsyncPersistenceProcessor<R> {

	protected RetryerInfo<R> retryerInfo;

//	private RetryConfiguration retryConfiguration;

	public AbstractAsyncPersistenceBeforeRetryProcessor(
		RetryerInfo<R> retryerInfo) {
		this.retryerInfo = retryerInfo;
//		this.retryConfiguration = retryConfiguration;
	}

	@Override
	public void doProcess() {
		RetryConfiguration retryConfiguration = retryerInfo.getRetryConfiguration();
		ArgSerializerInfo argSerializerInfo = new ArgSerializerInfo();
		argSerializerInfo.setArgs(retryerInfo.getArgs());
		argSerializerInfo.setExecutorMethodName(retryerInfo.getExecutorMethodName());
		argSerializerInfo.setExecutorName(retryerInfo.getExecutorName());
		String argsStr = retryConfiguration.getRetrySerializerAccess()
			.getCurrentGlobalRetrySerializer().serialize(argSerializerInfo);

		RetryTask retryTask = new RetryTask();
		retryTask.setBizId(retryerInfo.getBizId());
		retryTask.setArgsStr(argsStr);
		retryTask.setStatus(RetryTaskStatusEnum.INIT);
		retryTask.setExecutorMethodName(retryerInfo.getExecutorMethodName());
		retryTask.setExecutorName(retryerInfo.getExecutorName());
		retryTask.setOnFailureMethod(retryerInfo.getOnFailureMethod());
		retryTask.setGmtCreate(new Date());
		retryTask.setGmtModified(new Date());

		Map<String, String> extAttrs = Maps.newHashMap();
		if (Objects.nonNull(retryerInfo.getResultPredicate())) {
			extAttrs.put("resultPredicateSerializer",
				retryConfiguration.getResultPredicateSerializer()
					.serialize(retryerInfo.getResultPredicate()));
		}
		retryTask.setExtAttrs(extAttrs);

		PrepSaveBeforeRetryEvent prepSaveBeforeRetryEvent = new PrepSaveBeforeRetryEvent(retryTask);
		retryConfiguration.getRetryEventMulticaster().multicast(prepSaveBeforeRetryEvent);

		retryConfiguration.getRetryTaskAccess().saveRetryTask(retryTask);

		AfterSaveBeforeRetryEvent afterSaveBeforeRetryEvent = new AfterSaveBeforeRetryEvent(
			retryTask);
		retryConfiguration.getRetryEventMulticaster().multicast(afterSaveBeforeRetryEvent);
	}

}
