package com.alibaba.easyretry.extension.mybatis.dao;

import com.alibaba.easyretry.common.constant.enums.RetryTaskStatusEnum;
import com.alibaba.easyretry.extension.mybatis.MyBatisConfig;
import com.alibaba.easyretry.extension.mybatis.po.RetryTaskPO;
import com.alibaba.easyretry.extension.mybatis.query.RetryTaskQuery;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

class RetryTaskDAOImplTest {

	private static RetryTaskDAO retryTaskDAO;

	@BeforeAll
	static void prepare() {
		retryTaskDAO = new RetryTaskDAOImpl(MyBatisConfig.getFactory());
	}

	@Test
	@Order(1)
	void saveTask() {
		final RetryTaskPO retryTaskPO = new RetryTaskPO()
			.setId(1L)
			.setGmtCreate(new Date())
			.setBizId("1")
			.setRetryStatus(RetryTaskStatusEnum.HANDLING.getCode())
			.setGmtModified(new Date());

		final boolean result = retryTaskDAO.saveRetryTask(retryTaskPO);
		Assertions.assertTrue(result);
	}

	@Test
	@Order(2)
	void listRetryTask() {
		final RetryTaskPO retryTaskPO = new RetryTaskPO()
			.setId(1L)
			.setGmtCreate(new Date())
			.setBizId("1")
			.setRetryStatus(RetryTaskStatusEnum.HANDLING.getCode())
			.setGmtModified(new Date());

		retryTaskDAO.saveRetryTask(retryTaskPO);
		final RetryTaskQuery retryTaskQuery = new RetryTaskQuery()
			.setRetryStatus(Collections.singletonList(RetryTaskStatusEnum.HANDLING.getCode()));
		List<RetryTaskPO> retryTaskPOS = retryTaskDAO.listRetryTask(retryTaskQuery);
		System.out.println(retryTaskPOS);
		Assertions.assertTrue(!retryTaskPOS.isEmpty());
		Assertions.assertTrue(Objects.nonNull(retryTaskPOS));
		// Assertions.assertTrue(Objects.nonNull(retryTaskPOS) && !retryTaskPOS.isEmpty());
	}

	@Test
	@Order(3)
	void updateRetryTask() {
		//final RetryTaskPO retryTaskPO = new RetryTaskPO()
		//	.setId(1L)
		//	.setBizId("1")
		//	.setRetryStatus(RetryTaskStatusEnum.FINISH.getCode())
		//	.setGmtModified(new Date());
		//
		//boolean b = retryTaskDAO.updateRetryTask(retryTaskPO);
		//Assertions.assertTrue(b);
		//
		//final RetryTaskQuery retryTaskQuery = new RetryTaskQuery()
		//	.setRetryStatus(Collections.singletonList(RetryTaskStatusEnum.FINISH.getCode()));
		//List<RetryTaskPO> retryTaskPOS = retryTaskDAO.listRetryTask(retryTaskQuery);
		//System.out.println(retryTaskPOS);
		//Assertions.assertTrue(Objects.nonNull(retryTaskPOS) && !retryTaskPOS.isEmpty());
	}

	@Test
	@Order(4)
	void deleteRetryTask() {
		final RetryTaskPO retryTaskPO = new RetryTaskPO()
			.setId(1L)
			.setGmtCreate(new Date())
			.setBizId("1")
			.setRetryStatus(RetryTaskStatusEnum.HANDLING.getCode())
			.setGmtModified(new Date());

		retryTaskDAO.saveRetryTask(retryTaskPO);
		boolean b = retryTaskDAO.deleteRetryTask(retryTaskPO);
		Assertions.assertTrue(b);

		final RetryTaskQuery retryTaskQuery = new RetryTaskQuery()
			.setRetryStatus(Collections.singletonList(RetryTaskStatusEnum.FINISH.getCode()));
		List<RetryTaskPO> retryTaskPOS = retryTaskDAO.listRetryTask(retryTaskQuery);
		System.out.println(retryTaskPOS);
		Assertions.assertTrue(Objects.isNull(retryTaskPOS) || retryTaskPOS.isEmpty());
	}

}
