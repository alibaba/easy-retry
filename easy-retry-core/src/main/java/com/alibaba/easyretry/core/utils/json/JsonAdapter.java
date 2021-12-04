package com.alibaba.easyretry.core.utils.json;

public interface JsonAdapter {

	String toJSONString(Object object);

	<T> T parseObject(String str, Class<T> clazz);
}
