package com.alibaba.easyretry.extension.spring.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EasyRetryable {

    /**
     * 重试失败后最终调用的方法,方法格式应该为  public void onRetryFailed(RetryContext context)
     *
     * @return
     */
    String onFailureMethod() default "";

    /**
     * 最大重试次数
     *
     * @return
     */
    int maxRetryTimes() default 10;

}
