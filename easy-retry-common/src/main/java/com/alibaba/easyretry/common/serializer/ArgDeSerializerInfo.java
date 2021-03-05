package com.alibaba.easyretry.common.serializer;

import lombok.Data;

/**
 * @author Created by wuhao on 2020/11/1.
 */
@Data
public class ArgDeSerializerInfo {

    /**
     * 执行者名称
     */
    private String executorName;

    /**
     * 执行者方法
     */
    private String executorMethodName;

    private String argsStr;
}
