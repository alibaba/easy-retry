package com.alibaba.easyretry.core.utils.json;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JsonFactoryTest {

	@Test
	void getImpl() {
		final JsonAdapter impl = JsonFactory.getImpl();
		assertTrue(impl instanceof JacksonImpl);
	}
}
