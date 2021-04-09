package com.alibaba.easyretry.core.utils;

import java.lang.reflect.Method;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BeanUtilsTest {

	/**
	 * fixme BeanUtils 底层实现可以用MethodUtils换掉
	 */
	@Test
	void getMethod() {
		Method say = BeanUtils.getMethod("say", TestClass.class);
		Method say1 = MethodUtils.getMatchingMethod(TestClass.class, "say", null);

		Assertions.assertEquals(say.getName(), say1.getName());
		Assertions.assertEquals(say.getModifiers(), say1.getModifiers());
	}
}
