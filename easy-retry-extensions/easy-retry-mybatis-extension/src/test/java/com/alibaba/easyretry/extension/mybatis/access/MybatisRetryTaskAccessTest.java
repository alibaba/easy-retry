package com.alibaba.easyretry.extension.mybatis.access;

import com.alibaba.easyretry.common.entity.RetryTask;
import com.alibaba.easyretry.extension.mybatis.MyBatisConfig;
import com.alibaba.easyretry.extension.mybatis.dao.RetryTaskDAOImpl;
import org.junit.jupiter.api.Test;

class MybatisRetryTaskAccessTest {

	private static final MybatisRetryTaskAccess ACCESS = new MybatisRetryTaskAccess(
		new RetryTaskDAOImpl(
			MyBatisConfig.getFactory()));

	@Test
	void test() {
		//ACCESS.saveRetryTask(new RetryTask());
	}

}
