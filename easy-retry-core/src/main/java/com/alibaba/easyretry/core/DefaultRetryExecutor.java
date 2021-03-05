package com.alibaba.easyretry.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.RetryExecutor;
import com.alibaba.easyretry.common.RetryIdentify;
import com.alibaba.easyretry.common.access.RetryTaskAccess;
import com.alibaba.easyretry.common.constant.enums.HandleResultEnum;
import com.alibaba.easyretry.common.entity.RetryTask;
import com.alibaba.easyretry.core.utils.BeanUtils;
import com.alibaba.easyretry.core.utils.LogUtils;
import com.alibaba.easyretry.core.utils.PrintUtils;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Created by wuhao on 2021/3/2.
 */
@Slf4j
public class DefaultRetryExecutor implements RetryExecutor {

    @Setter
    private RetryConfiguration retryConfiguration;

    @Override
    public HandleResultEnum doExecute(RetryContext context) {
        try {
            return handle(context);
        } catch (Throwable e) {
            log.error("Retry invoke failed", e);
            return HandleResultEnum.ERROR;
        }
    }

    private HandleResultEnum handle(RetryContext context) {
        if (context.getWaitStrategy().shouldWait(context)) {
            return HandleResultEnum.WAITING;
        }
        retryConfiguration.getRetryTaskAccess().handlingRetryTask(context.getRetryTask());
        try {
            RetryIdentify.start();
            executeMethod(context);
            finish(context);
            return HandleResultEnum.SUCCESS;
        } catch (Throwable t) {
            if (t instanceof InvocationTargetException) {
                t = t.getCause();
            }
            String errorMsg = PrintUtils.printCommonMethodInfo(context);
            if (context.getStopStrategy().shouldStop(context)) {
                stop(context);
                log.error(errorMsg + "will stop", t);
                return HandleResultEnum.STOP;
            } else {
                log.error(errorMsg + "will try later", t);
                context.getWaitStrategy().backOff(context);
                return HandleResultEnum.FAILURE;
            }
        } finally {
            RetryIdentify.stop();
        }
    }

    private void executeMethod(RetryContext context) throws InvocationTargetException, IllegalAccessException {
        Object executor = context.getExecutor();
        context.getMethod().invoke(executor, context.getArgs());
    }

    private void finish(RetryContext context) {
        RetryTask retryTask = context.getRetryTask();
        try {
            RetryTaskAccess retryTaskAccess = retryConfiguration.getRetryTaskAccess();
            retryTaskAccess.finishRetryTask(retryTask);
            context.stop();
        } catch (Throwable t) {
            LogUtils.CONSISTENCY_LOGGER.error(
                "finishRetryTask error" + PrintUtils.printCommonMethodInfo(context) + " please check", t);
        }
    }

    private void stop(RetryContext context) {
        try {
            RetryTaskAccess retryTaskAccess = retryConfiguration.getRetryTaskAccess();
            retryTaskAccess.stopRetryTask(context.getRetryTask());
            executeOnFailureMethod(context);
            context.stop();
        } catch (Throwable t) {
            LogUtils.CONSISTENCY_LOGGER.error(
                "stopRetryTask error" + PrintUtils.printCommonMethodInfo(context) + " please check", t);
        }
    }

    private void executeOnFailureMethod(RetryContext context) {
        String methodName = context.getOnFailureMethod();
        if (StringUtils.isEmpty(methodName)) {
            return;
        }
        Object executor = context.getExecutor();
        Method onFailure = BeanUtils.getMethod(methodName, executor.getClass());
        if (Objects.isNull(onFailure)) {
            return;
        }
        try {
            onFailure.invoke(executor, context);
        } catch (Throwable t) {
            LogUtils.CONSISTENCY_LOGGER.error(
                "executeOnFailureMethod failed onFailureMethod = {}" + PrintUtils.printCommonMethodInfo(context)
                    + " please check", methodName, t);
        }
    }
}
