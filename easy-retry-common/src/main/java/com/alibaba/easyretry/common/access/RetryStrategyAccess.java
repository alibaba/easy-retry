package com.alibaba.easyretry.common.access;

import com.alibaba.easyretry.common.strategy.StopStrategy;
import com.alibaba.easyretry.common.strategy.WaitStrategy;

/**
 * 重试策略获取 如果方法上有指定则用指定Strategy 否则使用Global
 *
 * @author Created by wuhao on 2020/11/6.
 */
public interface RetryStrategyAccess {

	/**
	 * 获取全局重试任务停止策略
	 * @return 停止策略
	 */
	StopStrategy getCurrentGlobalStopStrategy();

	/**
	 * 获取全局等待策略
	 * @return 等待策略
	 */
	WaitStrategy getCurrentGlobalWaitStrategy();

}
