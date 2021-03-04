package com.alibaba.easyretry.common;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import com.alibaba.easyretry.common.entity.RetryTask;
import com.alibaba.easyretry.common.serializer.ArgDeSerializerInfo;
import com.alibaba.easyretry.common.serializer.RetryArgSerializer;
import com.alibaba.easyretry.common.strategy.StopStrategy;
import com.alibaba.easyretry.common.strategy.WaitStrategy;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.reflect.MethodUtils;

@Data
@ToString(callSuper = true)
public class RetryContext implements Comparable<RetryContext>, RetryLifecycle {

    private RetryTask retryTask;

    private Object executor;

    private Method method;

    private Object[] args;

    private RetryArgSerializer retryArgSerializer;

    private Long priority;

    private StopStrategy stopStrategy;

    private WaitStrategy waitStrategy;

    private int maxRetryTimes;

    private String onFailureMethod;

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    @Override
    public int compareTo(RetryContext o) {
        return this.priority > o.getPriority() ? 1 : -1;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        stopStrategy.clear(this);
        waitStrategy.clear(this);
    }

    public static class RetryContextBuilder {

        private RetryContext retryContext;

        private RetryConfiguration retryConfiguration;

        private RetryTask retryTask;

        public RetryContextBuilder(RetryConfiguration retryConfiguration, RetryTask retryTask) {
            retryContext = new RetryContext();
            this.retryConfiguration = retryConfiguration;
            this.retryTask = retryTask;
        }

        public RetryContextBuilder buildArgs() {
            RetryArgSerializer retryArgSerializer = retryConfiguration.getRetrySerializerAccess().getRetrySerializer(retryTask);
            ArgDeSerializerInfo argDeSerializerInfo = new ArgDeSerializerInfo();
            argDeSerializerInfo.setArgsStr(retryTask.getArgsStr());
            argDeSerializerInfo.setExecutorMethodName(retryTask.getExecutorMethodName());
            argDeSerializerInfo.setExecutorName(retryTask.getExecutorName());
            Object[] args = retryArgSerializer.deSerialize(argDeSerializerInfo);
            retryContext.setArgs(args);
            return this;
        }

        public RetryContextBuilder buildExecutor() {
            Object executor = retryConfiguration.getExecutorSolver().resolver(retryTask.getExecutorName());
            retryContext.setExecutor(executor);
            return this;
        }

        public RetryContextBuilder buildMethod() {
            Object[] objects = retryContext.getArgs();
            Class<?>[] classes = Stream.of(objects).map(Object::getClass).toArray(Class[]::new);
            Method method = MethodUtils.getMatchingMethod(retryContext.getExecutor().getClass(), retryTask.getExecutorMethodName(), classes);
            retryContext.setMethod(method);
            return this;
        }

        public RetryContextBuilder buildRetryArgSerializer() {
            retryContext.setRetryArgSerializer(retryConfiguration.getRetrySerializerAccess().getRetrySerializer(retryTask));
            return this;
        }

        public RetryContextBuilder buildStopStrategy() {
            retryContext.setStopStrategy(retryConfiguration.getRetryStrategyAccess().getStopStrategy(retryTask));
            return this;
        }

        public RetryContextBuilder buildWaitStrategy() {
            retryContext.setWaitStrategy(retryConfiguration.getRetryStrategyAccess().getWaitStrategy(retryTask));
            return this;
        }

        public RetryContextBuilder buildRetryTask() {
            retryContext.setRetryTask(retryTask);
            return this;
        }

        public RetryContextBuilder buildMaxRetryTimes() {
            retryContext.setMaxRetryTimes(retryConfiguration.getMaxRetryTimes());
            return this;
        }

        public RetryContextBuilder buildOnFailureMethod() {
            retryContext.setOnFailureMethod(retryTask.getOnFailureMethod());
            return this;
        }

        public RetryContextBuilder buildPriority(Long priority) {
            retryContext.setPriority(priority);
            return this;
        }

        public RetryContext build() {
            return retryContext;
        }
    }
}
