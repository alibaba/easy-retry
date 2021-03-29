package com.alibaba.easyretry.extension.mybatis.common.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HostUtilsTest {

	@Test
	void getHostIP() {
		String hostIP = HostUtils.getHostIP();
		assertNotNull(hostIP);
	}
}
