package com.alibaba.easyretry.common;

import com.alibaba.easyretry.common.constant.enums.RetryTaskStatusEnum;
import com.alibaba.easyretry.common.entity.RetryTask;
import com.alibaba.easyretry.common.serializer.ArgSerializerInfo;
import com.google.common.collect.Maps;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;

/**
 * @author Created by wuhao on 2020/11/1.
 */
@Slf4j
@Data
public class PersistenceRetryer implements Retryer {

	/**
	 * 执行者名称
	 */
	private String executorName;

	/**
	 * 执行者方法
	 */
	private String executorMethodName;

	private String onFailureMethod;

	/**
	 * 业务id，外部可以自定义存储一些信息
	 */
	private String bizId;

	private Object[] args;

	private Class<? extends Throwable> onException;

	private RetryConfiguration retryConfiguration;

	private String namespace;

	private boolean reThrowException;

	private ResultPredicate resultPredicate;

	public <V> V call(SCallable<V> callable) throws Throwable {
		try {
			V result = callable.call();
			if (Objects.nonNull(resultPredicate)) {
				if (resultPredicate.apply(result)) {
					saveRetryTask();
				}
			}
			return result;
		} catch (Throwable e) {
			log.error(
				"call method error executorMethodName is {} executorName name is {} args is {}",
				executorMethodName,
				executorName,
				args,
				e);
			handleException(e);
			if (reThrowException) {
				throw e;
			}
			return null;
		}
	}

	private void handleException(Throwable e) {
		if (Objects.nonNull(onException) && !ClassUtils.isAssignable(onException, e.getClass())) {
			return;
		}
		saveRetryTask();
	}

	private void saveRetryTask() {
		ArgSerializerInfo argSerializerInfo = new ArgSerializerInfo();
		argSerializerInfo.setArgs(args);
		argSerializerInfo.setExecutorMethodName(executorMethodName);
		argSerializerInfo.setExecutorName(executorName);
		String argsStr =
			retryConfiguration
				.getRetrySerializerAccess()
				.getCurrentGlobalRetrySerializer()
				.serialize(argSerializerInfo);

		RetryTask retryTask = new RetryTask();
		retryTask.setBizId(bizId);
		retryTask.setNamespace(namespace);
		retryTask.setArgsStr(argsStr);
		retryTask.setStatus(RetryTaskStatusEnum.INIT);
		retryTask.setExecutorMethodName(executorMethodName);
		retryTask.setExecutorName(executorName);
		retryTask.setOnFailureMethod(onFailureMethod);
		retryTask.setGmtCreate(new Date());
		retryTask.setGmtModified(new Date());

		Map<String, String> extAttrs = Maps.newHashMap();
		if(Objects.nonNull(resultPredicate)){
			extAttrs.put("resultPredicateSerializer",
				retryConfiguration.getResultPredicateSerializer().serialize(resultPredicate));
		}
		retryTask.setExtAttrs(extAttrs);
		retryConfiguration.getRetryTaskAccess().saveRetryTask(retryTask);
	}
}
