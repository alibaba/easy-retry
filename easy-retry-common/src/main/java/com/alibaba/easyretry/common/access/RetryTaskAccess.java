package com.alibaba.easyretry.common.access;

import java.util.List;

import com.alibaba.easyretry.common.entity.RetryTask;

/**
 * 重试任务获取器
 */
public interface RetryTaskAccess {

    /**
     * 保存重试任务
     * @param retryTask
     * @return
     */
    boolean saveRetryTask(RetryTask retryTask);

    /**
     * 更改重试任务为处理中
     * @param retryTask
     * @return
     */
    boolean handlingRetryTask(RetryTask retryTask);

    /**
     * 完结重试任务
     * @param retryTask
     * @return
     */
    boolean finishRetryTask(RetryTask retryTask);

    /**
     * 停止重试任务
     * @param retryTask
     * @return
     */
    boolean stopRetryTask(RetryTask retryTask);

    /**
     * 批量查询重试任务
     * @param namespace
     * @param lastId
     * @return
     */
    List<RetryTask> listAvailableTasks(String namespace, Long lastId);
}
