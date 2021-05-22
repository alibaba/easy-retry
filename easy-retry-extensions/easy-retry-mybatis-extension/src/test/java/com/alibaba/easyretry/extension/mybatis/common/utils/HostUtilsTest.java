package com.alibaba.easyretry.extension.mybatis.common.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class HostUtilsTest {

	@Test
	void getHostIP() {
		String hostIP = HostUtils.getHostIP();
		assertNotNull(hostIP);
	}
}
