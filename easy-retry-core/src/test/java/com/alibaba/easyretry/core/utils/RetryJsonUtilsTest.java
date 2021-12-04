package com.alibaba.easyretry.core.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class RetryJsonUtilsTest {

	@Test
	void toJson() {
		final String haha = RetryJsonUtils.toJSONString("haha");
		assertNotNull(haha);
		Map<String, Object> map = new HashMap<>();
		map.put("foo", 1);
		map.put("bar", 1);
		final String s = RetryJsonUtils.toJSONString(map);
		final Map map1 = RetryJsonUtils.parseObject(s, Map.class);
		assertNotNull(map1);
		assertEquals(map1.get("foo"), 1);
		assertEquals(map1.get("bar"), 1);
	}
}
