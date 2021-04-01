package com.alibaba.easyretry.extension.mybatis.dao;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.alibaba.easyretry.common.constant.enums.RetryTaskStatusEnum;
import com.alibaba.easyretry.extension.mybatis.MyBatisConfig;
import com.alibaba.easyretry.extension.mybatis.po.RetryTaskPO;
import com.alibaba.easyretry.extension.mybatis.query.RetryTaskQuery;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class RetryTaskDAOImplTest {

	private static RetryTaskDAO retryTaskDAO;

	@BeforeAll
	static void prepare() {
		retryTaskDAO = new RetryTaskDAOImpl(MyBatisConfig.getFactory());
	}

	@Test
	void saveRetryTask() {
		final RetryTaskPO retryTaskPO = new RetryTaskPO()
			.setId(1L)
			.setGmtCreate(new Date())
			.setBizId("1")
			.setRetryStatus(RetryTaskStatusEnum.HANDLING.getCode())
			.setGmtModified(new Date());

		final boolean result = retryTaskDAO.saveRetryTask(retryTaskPO);
		assertTrue(result);
	}

	@Test
	void listRetryTask() {
		final RetryTaskQuery retryTaskQuery = new RetryTaskQuery()
			.setRetryStatus(Collections.singletonList(RetryTaskStatusEnum.HANDLING.getCode()));
		List<RetryTaskPO> retryTaskPOS = retryTaskDAO.listRetryTask(retryTaskQuery);
		System.out.println(retryTaskPOS);
		Assertions.assertNotNull(retryTaskPOS);
	}

	@Test
	void updateRetryTask() {
	}

	@Test
	void deleteRetryTask() {
	}

}
