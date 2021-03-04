package com.alibaba.easyretry.extension.mybatis.access;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.easyretry.common.access.RetryTaskAccess;
import com.alibaba.easyretry.common.constant.enums.RetryTaskStatusEnum;
import com.alibaba.easyretry.common.entity.RetryTask;
import com.alibaba.easyretry.extension.mybatis.common.utils.HostUtils;
import com.alibaba.easyretry.extension.mybatis.dao.RetryTaskDAO;
import com.alibaba.easyretry.extension.mybatis.po.RetryTaskPO;
import com.alibaba.easyretry.extension.mybatis.query.RetryTaskQuery;
import com.alibaba.fastjson.JSON;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Setter;

/**
 * @author Created by wuhao on 2020/11/8.
 */
public class MybatisRetryTaskAccess implements RetryTaskAccess {

    @Setter
    private RetryTaskDAO retryTaskDAO;

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

        Map<String, String> ext = Maps.newHashMap();
        ext.put("onFailureMethod", retryTask.getOnFailureMethod());
        retryTaskPO.setExtAttrs(JSON.toJSONString(ext));

        retryTaskPO.setRetryStatus(retryTask.getStatus().getCode());
        retryTaskPO.setExtAttrs(retryTask.getExtAttrs());
        retryTaskPO.setArgsStr(retryTask.getArgsStr());
        retryTaskPO.setNamespace(retryTask.getNamespace());
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
    public List<RetryTask> listAvailableTasks(String namespace, Long lastId) {
        RetryTaskQuery retryTaskQuery = new RetryTaskQuery();
        retryTaskQuery.setRetryStatus(Lists.newArrayList(RetryTaskStatusEnum.INIT.getCode(), RetryTaskStatusEnum.HANDLING.getCode()));
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
        retryTask.setNamespace(retryTaskPO.getNamespace());
        retryTask.setGmtCreate(retryTaskPO.getGmtCreate());
        retryTask.setGmtModified(retryTaskPO.getGmtModified());
        retryTask.setExecutorName(retryTaskPO.getExecutorName());
        retryTask.setExecutorMethodName(retryTaskPO.getExecutorMethodName());
        return retryTask;
    }

}
