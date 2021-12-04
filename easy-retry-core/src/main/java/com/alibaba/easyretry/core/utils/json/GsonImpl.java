package com.alibaba.easyretry.core.utils.json;

import com.google.gson.Gson;

class GsonImpl extends AbstractJsonAdapter {

	@Override
	public String toJSONString(Object object) {
		return getGson().toJson(object);
	}

	@Override
	public <T> T parseObject(String str, Class<T> clazz) {
		return getGson().fromJson(str, clazz);
	}

	private static Gson getGson() {
		return getInstanceFromSpringIfExist(Gson.class, Gson::new);
	}

}
