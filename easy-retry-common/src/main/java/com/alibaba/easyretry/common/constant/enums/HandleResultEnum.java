package com.alibaba.easyretry.common.constant.enums;

/**
 * 重试任务重试结果
 *
 * @author Created by wuhao on 2020/11/1.
 */
public enum HandleResultEnum {

    /**
     * 处理成功
     */
    SUCCESS,

    /**
     * 处理失败
     */
    FAILURE,

    /**
     * 等待策略返回该case还在等待队列中
     */
    WAITING,

    /**
     * 根据停止策略该笔case停止重试
     */
    STOP,

    ERROR;
}