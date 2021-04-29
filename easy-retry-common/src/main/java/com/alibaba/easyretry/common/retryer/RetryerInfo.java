package com.alibaba.easyretry.common.retryer;

import com.alibaba.easyretry.common.AbstractResultPredicate;
import com.alibaba.easyretry.common.RetryConfiguration;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Created by wuhao on 2021/3/19.
 */
@Data
@Accessors(chain = true)
public class RetryerInfo<T> {

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

	private AbstractResultPredicate<T> resultPredicate;

}
