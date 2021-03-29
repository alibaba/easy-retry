package com.alibaba.easyretry.common.serializer;

/**
 * @author Created by wuhao on 2021/3/18.
 */
public interface EasyRetrySerializer<T> {

	String serialize(T serializeInfo);

	T deSerialize(String infoStr);

}
