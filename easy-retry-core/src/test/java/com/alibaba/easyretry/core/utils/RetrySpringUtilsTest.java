package com.alibaba.easyretry.core.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RetrySpringUtilsTest {

	@Test
	void inSpringContext() {
		assertFalse(RetrySpringUtils.inSpringContext());
	}

	@Test
	void getBean() {
		assertFalse(RetrySpringUtils.getBean(String.class).isPresent());
	}
}
