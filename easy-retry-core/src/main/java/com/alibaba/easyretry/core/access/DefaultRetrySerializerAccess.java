package com.alibaba.easyretry.core.access;

import com.alibaba.easyretry.common.access.RetrySerializerAccess;
import com.alibaba.easyretry.common.entity.RetryTask;
import com.alibaba.easyretry.common.serializer.RetryArgSerializer;
import com.alibaba.easyretry.core.serializer.HessianSerializer;

/**
 * @author Created by wuhao on 2020/11/6.
 */
public class DefaultRetrySerializerAccess implements RetrySerializerAccess {

    @Override
    public RetryArgSerializer getCurrentGlobalRetrySerializer() {
        return new HessianSerializer();
    }

    @Override
    public RetryArgSerializer getRetrySerializer(RetryTask retryTask) {
        return new HessianSerializer();
    }
}
