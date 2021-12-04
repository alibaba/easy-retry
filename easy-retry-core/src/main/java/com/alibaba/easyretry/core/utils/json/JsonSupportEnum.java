package com.alibaba.easyretry.core.utils.json;

import java.util.function.Supplier;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum JsonSupportEnum {
	JACKSON("com.fasterxml.jackson.databind.ObjectMapper", JacksonImpl::new),
	GSON("com.google.gson.Gson", GsonImpl::new),
	FASTJSON("com.alibaba.fastjson.JSON", FastjsonImpl::new);

	final String mainClassName;

	final Supplier<JsonAdapter> impl;
}
