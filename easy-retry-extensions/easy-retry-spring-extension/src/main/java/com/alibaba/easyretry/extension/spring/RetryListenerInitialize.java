package com.alibaba.easyretry.extension.spring;

import java.util.Map;

import com.alibaba.easyretry.common.event.RetryEventMulticaster;
import com.alibaba.easyretry.common.event.RetryListener;

import lombok.Setter;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Created by wuhao on 2021/4/9.
 */
public class RetryListenerInitialize implements SmartInitializingSingleton, ApplicationContextAware {

	@Setter
	private RetryEventMulticaster retryEventMulticaster;

	private ApplicationContext applicationContext;

	@Override
	public void afterSingletonsInstantiated() {
		Map<String, RetryListener> retryListenerMap = applicationContext.getBeansOfType(RetryListener.class);
		MapUtils.emptyIfNull(retryListenerMap).values().forEach(
			(retryListener) -> retryEventMulticaster.register(retryListener));
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
