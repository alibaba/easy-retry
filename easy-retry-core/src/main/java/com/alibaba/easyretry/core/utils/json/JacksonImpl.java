package com.alibaba.easyretry.core.utils.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

class JacksonImpl extends AbstractJsonAdapter {

	@Override
	@SneakyThrows
	public String toJSONString(Object object) {
		return getMapper().writeValueAsString(object);
	}

	@Override
	@SneakyThrows
	public <T> T parseObject(String str, Class<T> clazz) {
		return getMapper().readValue(str, clazz);
	}

	private static ObjectMapper getMapper() {
		return getInstanceFromSpringIfExist(ObjectMapper.class, ObjectMapper::new);
	}

}
