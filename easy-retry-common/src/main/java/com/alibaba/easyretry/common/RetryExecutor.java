package com.alibaba.easyretry.common;

import com.alibaba.easyretry.common.constant.enums.HandleResultEnum;

/**
 * @author Created by wuhao on 2021/3/2.
 */
public interface RetryExecutor {

    HandleResultEnum doExecute(RetryContext context);

}
