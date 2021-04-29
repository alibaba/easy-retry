package com.alibaba.easyretry.common.access;

import com.alibaba.easyretry.common.entity.RetryTask;
import java.util.List;

/**
 * 重试任务获取器
 */
public interface RetryTaskAccess {

	/**
	 * 保存重试任务
	 */
	boolean saveRetryTask(RetryTask retryTask);

	/**
	 * 更改重试任务为处理中
	 */
	boolean handlingRetryTask(RetryTask retryTask);

	/**
	 * 完结重试任务
	 */
	boolean finishRetryTask(RetryTask retryTask);

	/**
	 * 停止重试任务
	 */
	boolean stopRetryTask(RetryTask retryTask);

	/**
	 * 批量查询重试任务
	 */
	List<RetryTask> listAvailableTasks(Long lastId);
}
