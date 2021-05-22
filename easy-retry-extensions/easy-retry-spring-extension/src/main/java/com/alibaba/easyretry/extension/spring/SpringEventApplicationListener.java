package com.alibaba.easyretry.extension.spring;

import com.alibaba.easyretry.common.filter.RetryFilterInvocationHandler;
import com.alibaba.easyretry.common.filter.RetryFilterRegisterHandler;

import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author Created by wuhao on 2021/4/9.
 */
public class SpringEventApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

	@Setter
	private RetryFilterInvocationHandler retryFilterInvocationHandler;

	@Setter
	private RetryFilterRegisterHandler retryFilterRegisterHandler;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext applicationContext = event.getApplicationContext();
		//防止父子容器场景
		if (applicationContext.getParent() != null) {
			return;
		}
		retryFilterRegisterHandler.handle();
		retryFilterInvocationHandler.handle();
	}
}
