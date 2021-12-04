package com.alibaba.easyretry.core.utils.json;

import com.alibaba.fastjson.JSON;

class FastjsonImpl implements JsonAdapter {
	@Override
	public String toJSONString(Object object) {
		return JSON.toJSONString(object);
	}

	@Override
	public <T> T parseObject(String str, Class<T> clazz) {
		return JSON.parseObject(str, clazz);
	}
}
