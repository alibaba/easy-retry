package com.alibaba.easyretry.extension.spring;

import org.springframework.beans.factory.SmartInitializingSingleton;

import com.alibaba.easyretry.common.filter.RetryFilterInvocationHandler;
import com.alibaba.easyretry.common.filter.RetryFilterRegisterHandler;

import lombok.Setter;

/**
 * @author Created by wuhao on 2021/4/9.
 */
public class SpringEventApplicationListener implements SmartInitializingSingleton {

	@Setter
	private RetryFilterInvocationHandler retryFilterInvocationHandler;

	@Setter
	private RetryFilterRegisterHandler retryFilterRegisterHandler;


	@Override
	public void afterSingletonsInstantiated() {
		retryFilterRegisterHandler.handle();
		retryFilterInvocationHandler.handle();
	}
}
