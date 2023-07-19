package com.alibaba.easyretry.common.access;

import java.util.List;

import com.alibaba.easyretry.common.entity.RetryTask;

/**
 * 重试任务获取器
 */
public interface RetryTaskAccess {

	/**
	 * 保存重试任务
	 * @param retryTask 重试任务
	 * @return 是否成功
	 */
	boolean saveRetryTask(RetryTask retryTask);

	/**
	 * 更改重试任务为处理中
	 * @param retryTask 重试任务
	 * @return 是否成功
	 */
	boolean handlingRetryTask(RetryTask retryTask);

	/**
	 * 完结重试任务
	 * @param retryTask 重试任务
	 * @return 是否成功
	 */
	boolean finishRetryTask(RetryTask retryTask);

	/**
	 * 停止重试任务
	 * @param retryTask 重试任务
	 * @return 是否成功
	 */
	boolean stopRetryTask(RetryTask retryTask);

	/**
	 * 批量查询重试任务
	 * @param lastId 查询id
	 * @return 重试任务列表
	 */
	List<RetryTask> listAvailableTasks(Long lastId);
}
