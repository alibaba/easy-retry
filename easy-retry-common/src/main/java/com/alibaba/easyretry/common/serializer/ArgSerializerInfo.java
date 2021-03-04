package com.alibaba.easyretry.common.serializer;

import lombok.Data;

/**
 * @author Created by wuhao on 2020/11/1.
 */
@Data
public class ArgSerializerInfo {

    /**
     * 执行者名称
     */
    private String executorName;

    private String executorClassName;

    /**
     * 执行者方法
     */
    private String executorMethodName;

    private Object[] args;
}
