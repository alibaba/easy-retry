package com.alibaba.easyretry.core.strategy;

import java.util.Map;
import java.util.Objects;

import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.entity.RetryTask;
import com.alibaba.easyretry.common.strategy.StopStrategy;
import com.alibaba.easyretry.common.strategy.WaitStrategy;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultRetryStrategy implements StopStrategy, WaitStrategy {

    private Map<Long, Long> internalTimeMap = Maps.newConcurrentMap();

    private Map<Long, Integer> retryTimeMap = Maps.newConcurrentMap();

    private final static Long MAX_INTERNAL_TIME = 15 * 60 * 1000L;

    private final static Long BASE_INTERNAL_TIME = 5000L;

    @Override
    public boolean shouldStop(RetryContext context) {
        RetryTask retryTask = context.getRetryTask();
        Integer retryTimes = retryTimeMap.get(retryTask.getId());
        if (Objects.isNull(retryTimes)) {
            retryTimes = 1;
        }
        log.warn("shouldStop retryTime is {} id is {} maxRetryTime is {}", retryTimes, retryTask.getId(),context.getMaxRetryTimes());
        return retryTimes >= context.getMaxRetryTimes();
    }

    @Override
    public boolean shouldWait(RetryContext context) {
        RetryTask retryTask = context.getRetryTask();
        internalTimeMap.putIfAbsent(retryTask.getId(), 0L);
        Long priority = context.getPriority();
        if (Objects.isNull(priority)) {
            priority = 0L;
        }
        return System.currentTimeMillis() < priority;
    }

    @Override
    public void backOff(RetryContext context) {
        RetryTask retryTask = context.getRetryTask();
        Long id = retryTask.getId();
        Integer retryTime = retryTimeMap.get(id);
        Long lastInternalTime = internalTimeMap.get(id);
        if(Objects.isNull(retryTime)){
            retryTime = 1;
        }
        if(Objects.isNull(lastInternalTime)){
            lastInternalTime = 0L;
        }
        long nextInternalTime = retryTime * (lastInternalTime + BASE_INTERNAL_TIME);
        nextInternalTime = Math.min(nextInternalTime, MAX_INTERNAL_TIME);

        internalTimeMap.put(id, nextInternalTime);

        retryTime++;
        retryTimeMap.put(id, retryTime);
        context.setPriority(System.currentTimeMillis() + nextInternalTime);
        log.warn("backOff nextInternalTime is {} id is {} retryTime is {}", nextInternalTime, retryTask.getId(),retryTime);

    }

    @Override
    public void clear(RetryContext context) {
        RetryTask retryTask = context.getRetryTask();
        internalTimeMap.remove(retryTask.getId());
        retryTimeMap.remove(retryTask.getId());
    }
}