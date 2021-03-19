package com.alibaba.easyretry.core.process.asyn;

import com.alibaba.easyretry.common.constant.enums.RetryTaskStatusEnum;
import com.alibaba.easyretry.common.entity.RetryTask;
import com.alibaba.easyretry.common.retryer.RetryerInfo;
import com.alibaba.easyretry.common.serializer.ArgSerializerInfo;
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

	public AbstractAsynPersistenceBeforeRetryProcessor(
		RetryerInfo retryerInfo) {
		this.retryerInfo = retryerInfo;
	}

	@Override
	public void doProcess() {
		ArgSerializerInfo argSerializerInfo = new ArgSerializerInfo();
		argSerializerInfo.setArgs(retryerInfo.getArgs());
		argSerializerInfo.setExecutorMethodName(retryerInfo.getExecutorMethodName());
		argSerializerInfo.setExecutorName(retryerInfo.getExecutorName());
		String argsStr =
			retryerInfo.getRetryConfiguration()
				.getRetrySerializerAccess()
				.getCurrentGlobalRetrySerializer()
				.serialize(argSerializerInfo);

		RetryTask retryTask = new RetryTask();
		retryTask.setBizId(retryerInfo.getBizId());
		retryTask.setNamespace(retryerInfo.getNamespace());
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
		retryerInfo.getRetryConfiguration().getRetryTaskAccess().saveRetryTask(retryTask);
	}

}
