package com.alibaba.easyretry.common.access;

import com.alibaba.easyretry.common.entity.RetryTask;
import com.alibaba.easyretry.common.strategy.StopStrategy;
import com.alibaba.easyretry.common.strategy.WaitStrategy;

/**
 * 重试策略获取
 * 如果方法上有指定则用指定Strategy
 * 否则使用Global
 *
 * @author Created by wuhao on 2020/11/6.
 */
public interface RetryStrategyAccess {

    /**
     * 获取全局重试任务停止策略
     *
     * @return
     */
    StopStrategy getCurrentGlobalStopStrategy();

    /**
     * 根据重试任务获取全局重试任务停止策略
     *
     * @param retryTask
     * @return
     */
    StopStrategy getStopStrategy(RetryTask retryTask);

    /**
     * 获取全局等待策略
     *
     * @return
     */
    WaitStrategy getCurrentGlobalWaitStrategy();

    /**
     * 根据重试任务获取获取等待策略
     *
     * @param retryTask
     * @return
     */
    WaitStrategy getWaitStrategy(RetryTask retryTask);
}
