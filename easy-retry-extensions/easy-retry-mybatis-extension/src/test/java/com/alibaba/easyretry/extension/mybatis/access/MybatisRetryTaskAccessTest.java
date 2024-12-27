package com.alibaba.easyretry.extension.mybatis.access;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.alibaba.easyretry.common.constant.enums.RetryTaskStatusEnum;
import com.alibaba.easyretry.common.entity.RetryTask;
import com.alibaba.easyretry.extension.mybatis.MyBatisConfig;
import com.alibaba.easyretry.extension.mybatis.dao.RetryTaskDAOImpl;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MybatisRetryTaskAccessTest {

	private static final MybatisRetryTaskAccess ACCESS = new MybatisRetryTaskAccess(
		new RetryTaskDAOImpl(
			MyBatisConfig.getFactory()));

	private static RetryTask task;

	@BeforeAll
	static void prepare() {
		RetryTask retryTask = new RetryTask();
		retryTask.setId(2L);
		retryTask.setGmtCreate(new Date());
		retryTask.setGmtModified(new Date());
		retryTask.setBizId("2");
		retryTask.setStatus(RetryTaskStatusEnum.INIT);
		task = retryTask;
	}

	@Test
	@Order(1)
	void saveTask() {
		Assertions.assertTrue(ACCESS.saveRetryTask(task));
	}

	@Test
	@Order(2)
	void handle() {
//		ACCESS.saveRetryTask(task);
		Assertions.assertTrue(ACCESS.handlingRetryTask(task));
		List<RetryTask> retryTasks = ACCESS.listAvailableTasks(1L);
		Assertions.assertTrue(Objects.nonNull(retryTasks) && !retryTasks.isEmpty());
		Assertions.assertEquals(retryTasks.get(0).getStatus(), RetryTaskStatusEnum.HANDLING);
	}

	@Test
	@Order(3)
	void stop() {
//		ACCESS.saveRetryTask(task);
		boolean b = ACCESS.stopRetryTask(task);
		Assertions.assertTrue(b);
		List<RetryTask> retryTasks = ACCESS.listAvailableTasks(1L);
		Assertions.assertTrue(Objects.isNull(retryTasks) || retryTasks.isEmpty());
	}

	@Test
	@Order(4)
	void finish(){
//		ACCESS.saveRetryTask(task);
		Assertions.assertTrue(ACCESS.finishRetryTask(task));
		List<RetryTask> retryTasks = ACCESS.listAvailableTasks(1L);
		Assertions.assertTrue(Objects.isNull(retryTasks) || retryTasks.isEmpty());
	}
}
