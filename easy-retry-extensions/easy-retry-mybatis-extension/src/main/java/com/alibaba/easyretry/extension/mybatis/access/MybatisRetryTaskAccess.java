package com.alibaba.easyretry.extension.mybatis.access;

import com.alibaba.easyretry.common.access.RetryTaskAccess;
import com.alibaba.easyretry.common.constant.enums.RetryTaskStatusEnum;
import com.alibaba.easyretry.common.entity.RetryTask;
import com.alibaba.easyretry.extension.mybatis.common.utils.HostUtils;
import com.alibaba.easyretry.extension.mybatis.dao.RetryTaskDAO;
import com.alibaba.easyretry.extension.mybatis.po.RetryTaskPO;
import com.alibaba.easyretry.extension.mybatis.query.RetryTaskQuery;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;

/**
 * @author Created by wuhao on 2020/11/8.
 */
@AllArgsConstructor
public class MybatisRetryTaskAccess implements RetryTaskAccess {

	private final RetryTaskDAO retryTaskDAO;

	@Override
	public boolean saveRetryTask(RetryTask retryTask) {
		RetryTaskPO retryTaskPO = covert(retryTask);
		return retryTaskDAO.saveRetryTask(retryTaskPO);
	}

	private RetryTaskPO covert(RetryTask retryTask) {
		RetryTaskPO retryTaskPO = new RetryTaskPO();
		retryTaskPO.setId(retryTask.getId());
		retryTaskPO.setSharding(HostUtils.getHostIP());
		retryTaskPO.setBizId(retryTask.getBizId());
		retryTaskPO.setExecutorName(retryTask.getExecutorName());
		retryTaskPO.setExecutorMethodName(retryTask.getExecutorMethodName());

		Map<String, String> extAttrs = retryTask.getExtAttrs();
		if (Objects.isNull(extAttrs)) {
			extAttrs = Maps.newHashMap();
		}
		extAttrs.put("onFailureMethod", retryTask.getOnFailureMethod());
		retryTaskPO.setExtAttrs(JSON.toJSONString(extAttrs));
		retryTaskPO.setRetryStatus(retryTask.getStatus().getCode());
		retryTaskPO.setArgsStr(retryTask.getArgsStr());
		retryTaskPO.setGmtCreate(retryTask.getGmtCreate());
		retryTaskPO.setGmtModified(retryTask.getGmtModified());
		return retryTaskPO;
	}

	@Override
	public boolean handlingRetryTask(RetryTask retryTask) {
		return updateRetryTaskStatus(retryTask, RetryTaskStatusEnum.HANDLING);
	}

	@Override
	public boolean finishRetryTask(RetryTask retryTask) {
		RetryTaskPO retryTaskPO = new RetryTaskPO();
		retryTaskPO.setId(retryTask.getId());
		retryTaskDAO.deleteRetryTask(retryTaskPO);
		return updateRetryTaskStatus(retryTask, RetryTaskStatusEnum.FINISH);
	}

	@Override
	public boolean stopRetryTask(RetryTask retryTask) {
		return updateRetryTaskStatus(retryTask, RetryTaskStatusEnum.ERROR);
	}

	private boolean updateRetryTaskStatus(RetryTask retryTask, RetryTaskStatusEnum status) {
		RetryTaskPO retryTaskPO = new RetryTaskPO();
		retryTaskPO.setId(retryTask.getId());
		retryTaskPO.setRetryStatus(status.getCode());
		return retryTaskDAO.updateRetryTask(retryTaskPO);
	}

	@Override
	public List<RetryTask> listAvailableTasks(Long lastId) {
		RetryTaskQuery retryTaskQuery = new RetryTaskQuery();
		retryTaskQuery.setRetryStatus(
			Lists.newArrayList(
				RetryTaskStatusEnum.INIT.getCode(), RetryTaskStatusEnum.HANDLING.getCode()));
		retryTaskQuery.setLastId(lastId);
		retryTaskQuery.setSharding(HostUtils.getHostIP());
		List<RetryTaskPO> retryTasks = retryTaskDAO.listRetryTask(retryTaskQuery);
		return convert(retryTasks);
	}

	private List<RetryTask> convert(List<RetryTaskPO> retryTasks) {
		return retryTasks.stream().map(this::convert).collect(Collectors.toList());
	}

	private RetryTask convert(RetryTaskPO retryTaskPO) {
		RetryTask retryTask = new RetryTask();
		retryTask.setId(retryTaskPO.getId());
		retryTask.setStatus(RetryTaskStatusEnum.fromCode(retryTaskPO.getRetryStatus()));
		retryTask.setArgsStr(retryTaskPO.getArgsStr());
		retryTask.setGmtCreate(retryTaskPO.getGmtCreate());
		retryTask.setGmtModified(retryTaskPO.getGmtModified());
		retryTask.setExecutorName(retryTaskPO.getExecutorName());
		retryTask.setExecutorMethodName(retryTaskPO.getExecutorMethodName());
		retryTask.setBizId(retryTaskPO.getBizId());
		retryTask.setExtAttrs(JSON.parseObject(retryTaskPO.getExtAttrs(),new TypeReference<Map<String, String>>() {}));
		return retryTask;
	}
}
