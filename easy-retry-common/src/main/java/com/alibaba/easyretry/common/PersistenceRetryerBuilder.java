package com.alibaba.easyretry.common;

/**
 * @author Created by wuhao on 2020/11/1.
 */
public class PersistenceRetryerBuilder {

    /**
     * 执行者名称
     */
    private String executorName;

    /**
     * 执行者方法
     */
    private String executorMethodName;

    private String onFailureMethod;

    /**
     * 业务id，外部可以自定义存储一些信息
     */
    private String bizId;

    private Object[] args;

    private Class<? extends Throwable> onException;

    private RetryConfiguration retryConfiguration;

    private String namespace;

    public static PersistenceRetryerBuilder of() {
        return new PersistenceRetryerBuilder();
    }

    public PersistenceRetryerBuilder withExecutorName(String executorName) {
        this.executorName = executorName;
        return this;
    }

    public PersistenceRetryerBuilder withExecutorMethodName(String executorMethodName) {
        this.executorMethodName = executorMethodName;
        return this;
    }

    public PersistenceRetryerBuilder withBizId(String bizId) {
        this.bizId = bizId;
        return this;
    }

    public PersistenceRetryerBuilder withArgs(Object[] args) {
        this.args = args;
        return this;
    }

    public PersistenceRetryerBuilder withOnException(Class<? extends Throwable> onException) {
        this.onException = onException;
        return this;
    }

    public PersistenceRetryerBuilder withOnFailureMethod(String onFailureMethod) {
        this.onFailureMethod = onFailureMethod;
        return this;
    }

    public PersistenceRetryerBuilder withNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public PersistenceRetryerBuilder withConfiguration(RetryConfiguration retryConfiguration) {
        this.retryConfiguration = retryConfiguration;
        return this;
    }

    public PersistenceRetryer build() {
        PersistenceRetryer persistenceRetryer = new PersistenceRetryer();
        persistenceRetryer.setArgs(args);
        persistenceRetryer.setBizId(bizId);
        persistenceRetryer.setExecutorMethodName(executorMethodName);
        persistenceRetryer.setOnFailureMethod(onFailureMethod);
        persistenceRetryer.setOnException(onException);
        persistenceRetryer.setRetryConfiguration(retryConfiguration);
        persistenceRetryer.setExecutorName(executorName);
        persistenceRetryer.setNamespace(namespace);
        return persistenceRetryer;
    }

}
