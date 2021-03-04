package com.alibaba.easyretry.core.access;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.alibaba.easyretry.common.access.RetryTaskAccess;
import com.alibaba.easyretry.common.constant.enums.RetryTaskStatusEnum;
import com.alibaba.easyretry.common.entity.RetryTask;

import com.google.common.collect.Maps;

/**
 * @author Created by wuhao on 2020/11/5.
 */
public class MemoryRetryTaskAccess implements RetryTaskAccess {

    private static Map<Long, RetryTask> retryTaskMap = Maps.newConcurrentMap();

    private static AtomicLong atomicLong = new AtomicLong();

    @Override
    public boolean saveRetryTask(RetryTask retryTask) {
        long id = atomicLong.getAndIncrement();
        retryTask.setId(id);
        retryTaskMap.putIfAbsent(id, retryTask);
        return true;
    }

    @Override
    public boolean handlingRetryTask(RetryTask retryTask) {
        retryTask.setStatus(RetryTaskStatusEnum.HANDLING);
        retryTaskMap.putIfAbsent(retryTask.getId(), retryTask);
        return true;
    }

    @Override
    public boolean finishRetryTask(RetryTask retryTask) {
         retryTaskMap.remove(retryTask.getId());
         return true;
    }

    @Override
    public boolean stopRetryTask(RetryTask retryTask) {
        retryTask.setStatus(RetryTaskStatusEnum.ERROR);
        retryTaskMap.putIfAbsent(retryTask.getId(), retryTask);
        return false;
    }

    @Override
    public List<RetryTask> listAvailableTasks(String namespace, Long lastId) {
        return retryTaskMap.values().stream().filter(
            (retryTask) -> retryTask.getStatus() == RetryTaskStatusEnum.INIT).collect(Collectors.toList());
    }
}
