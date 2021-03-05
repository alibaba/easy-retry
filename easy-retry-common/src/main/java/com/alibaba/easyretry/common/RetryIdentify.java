package com.alibaba.easyretry.common;

import java.util.Objects;

/**
 * @author Created by wuhao on 2021/2/20.
 */
public class RetryIdentify {

    private static final ThreadLocal<String> RETRY_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    private static final String RETRY_FLAG = "RETRY_FLAG";

    public static void start() {
        RETRY_CONTEXT_THREAD_LOCAL.set(RETRY_FLAG);
    }

    public static void stop() {
        RETRY_CONTEXT_THREAD_LOCAL.set(null);
    }

    public static boolean isOnRetry() {
        return Objects.nonNull(RETRY_CONTEXT_THREAD_LOCAL.get());
    }
}
