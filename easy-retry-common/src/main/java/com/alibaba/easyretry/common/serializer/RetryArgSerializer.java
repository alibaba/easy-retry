package com.alibaba.easyretry.common.serializer;

/**
 * Retry 序列化器
 */
public interface RetryArgSerializer {

    String serialize(ArgSerializerInfo argSerializerInfo);

    Object[] deSerialize(ArgDeSerializerInfo argDeSerializerInfo);

}
