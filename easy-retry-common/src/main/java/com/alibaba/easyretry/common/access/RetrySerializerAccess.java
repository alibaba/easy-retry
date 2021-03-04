package com.alibaba.easyretry.common.access;

import com.alibaba.easyretry.common.entity.RetryTask;
import com.alibaba.easyretry.common.serializer.RetryArgSerializer;

/**
 * 重试信息序列化器获取
 * 如果方法上有指定序列化器，则使用getRetrySerializer
 * 否则使用全局序列化器getCurrentGlobalRetrySerializer
 *
 * @author Created by wuhao on 2020/11/6.
 */
public interface RetrySerializerAccess {

    /**
     * 获取全局序列化器
     *
     * @return
     */
    RetryArgSerializer getCurrentGlobalRetrySerializer();

    /**
     * 根据重试任务获取序列化器
     *
     * @param retryTask
     * @return
     */
    RetryArgSerializer getRetrySerializer(RetryTask retryTask);

}
