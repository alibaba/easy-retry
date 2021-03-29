package com.alibaba.easyretry.core.process.asyn.before;

import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.constant.enums.RetryTaskStatusEnum;
import com.alibaba.easyretry.common.entity.RetryTask;
import com.alibaba.easyretry.common.event.before.AfterSaveBeforeRetryEvent;
import com.alibaba.easyretry.common.event.before.PrepSaveBeforeRetryEvent;
import com.alibaba.easyretry.common.retryer.RetryerInfo;
import com.alibaba.easyretry.common.serializer.ArgSerializerInfo;
import com.alibaba.easyretry.core.process.asyn.AbstractAsynPersistenceProcessor;
import com.google.common.collect.Maps;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Created by wuhao on 2021/3/19.
 */
@Slf4j
public abstract class AbstractAsynPersistenceBeforeRetryProcessor<R> extends
	AbstractAsynPersistenceProcessor<R> {

	protected RetryerInfo retryerInfo;

	private RetryConfiguration retryConfiguration;

	public AbstractAsynPersistenceBeforeRetryProcessor(
		RetryerInfo retryerInfo, RetryConfiguration retryConfiguration) {
		this.retryerInfo = retryerInfo;
		this.retryConfiguration = retryConfiguration;
	}

	@Override
	public void doProcess() {
		ArgSerializerInfo argSerializerInfo = new ArgSerializerInfo();
		argSerializerInfo.setArgs(retryerInfo.getArgs());
		argSerializerInfo.setExecutorMethodName(retryerInfo.getExecutorMethodName());
		argSerializerInfo.setExecutorName(retryerInfo.getExecutorName());
		String argsStr = retryerInfo.getRetryConfiguration().getRetrySerializerAccess()
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
				retryerInfo.getRetryConfiguration().getResultPredicateSerializer()
					.serialize(retryerInfo.getResultPredicate()));
		}
		retryTask.setExtAttrs(extAttrs);

		PrepSaveBeforeRetryEvent prepSaveBeforeRetryEvent = new PrepSaveBeforeRetryEvent(retryTask);
		retryConfiguration.getRetryEventMulticaster().multicast(prepSaveBeforeRetryEvent);

		retryerInfo.getRetryConfiguration().getRetryTaskAccess().saveRetryTask(retryTask);

		AfterSaveBeforeRetryEvent afterSaveBeforeRetryEvent = new AfterSaveBeforeRetryEvent(
			retryTask);
		retryConfiguration.getRetryEventMulticaster().multicast(afterSaveBeforeRetryEvent);
	}

}
