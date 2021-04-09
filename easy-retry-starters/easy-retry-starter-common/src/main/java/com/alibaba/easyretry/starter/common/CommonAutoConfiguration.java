package com.alibaba.easyretry.starter.common;

import javax.sql.DataSource;

import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.RetryContainer;
import com.alibaba.easyretry.common.RetryExecutor;
import com.alibaba.easyretry.common.access.RetrySerializerAccess;
import com.alibaba.easyretry.common.access.RetryStrategyAccess;
import com.alibaba.easyretry.common.access.RetryTaskAccess;
import com.alibaba.easyretry.common.event.RetryEventMulticaster;
import com.alibaba.easyretry.common.filter.RetryFilterDiscover;
import com.alibaba.easyretry.common.filter.RetryFilterInvocation;
import com.alibaba.easyretry.common.filter.RetryFilterInvocationHandler;
import com.alibaba.easyretry.common.filter.RetryFilterRegister;
import com.alibaba.easyretry.common.filter.RetryFilterRegisterHandler;
import com.alibaba.easyretry.common.resolve.ExecutorSolver;
import com.alibaba.easyretry.common.serializer.ResultPredicateSerializer;
import com.alibaba.easyretry.common.strategy.StopStrategy;
import com.alibaba.easyretry.common.strategy.WaitStrategy;
import com.alibaba.easyretry.core.PersistenceRetryExecutor;
import com.alibaba.easyretry.core.access.DefaultRetrySerializerAccess;
import com.alibaba.easyretry.core.container.SimpleRetryContainer;
import com.alibaba.easyretry.core.event.SimpleRetryEventMulticaster;
import com.alibaba.easyretry.core.filter.DefaultRetryFilterInvocationHandler;
import com.alibaba.easyretry.core.filter.DefaultRetryFilterRegisterHandler;
import com.alibaba.easyretry.core.filter.SimpleRetryFilterRegister;
import com.alibaba.easyretry.core.serializer.HessianResultPredicateSerializer;
import com.alibaba.easyretry.core.strategy.DefaultRetryStrategy;
import com.alibaba.easyretry.extension.spring.RetryListenerInitialize;
import com.alibaba.easyretry.extension.spring.SpringEventApplicationListener;
import com.alibaba.easyretry.extension.spring.SpringRetryFilterDiscover;
import com.alibaba.easyretry.extension.spring.aop.RetryInterceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

/**
 * @author Created by wuhao on 2021/2/19.
 */
@Slf4j
public abstract class CommonAutoConfiguration implements ApplicationContextAware{

	protected ApplicationContext applicationContext;

	@Bean
	@ConditionalOnMissingBean(RetryConfiguration.class)
	public RetryConfiguration configuration(RetryTaskAccess retryTaskAccess,
		RetryEventMulticaster retryEventMulticaster) {
		DefaultRetryStrategy defaultRetryStrategy = new DefaultRetryStrategy();
		return new RetryConfiguration() {
			@Override
			public RetryTaskAccess getRetryTaskAccess() {
				return retryTaskAccess;
			}

			@Override
			public RetrySerializerAccess getRetrySerializerAccess() {
				return new DefaultRetrySerializerAccess();
			}

			@Override
			public RetryStrategyAccess getRetryStrategyAccess() {
				return new RetryStrategyAccess() {

					@Override
					public StopStrategy getCurrentGlobalStopStrategy() {
						return defaultRetryStrategy;
					}

					@Override
					public WaitStrategy getCurrentGlobalWaitStrategy() {
						return defaultRetryStrategy;
					}
				};
			}

			@Override
			public ExecutorSolver getExecutorSolver() {
				return executorName -> applicationContext.getBean(executorName);
			}

			@Override
			public ResultPredicateSerializer getResultPredicateSerializer() {
				return new HessianResultPredicateSerializer();
			}

			@Override
			public Integer getMaxRetryTimes() {
				return CommonAutoConfiguration.this.getMaxRetryTimes();
			}

			@Override
			public RetryEventMulticaster getRetryEventMulticaster() {
				return retryEventMulticaster;
			}
		};
	}

	@Bean
	@ConditionalOnMissingBean(RetryInterceptor.class)
	public RetryInterceptor retryInterceptor(RetryConfiguration configuration) {
		RetryInterceptor retryInterceptor = new RetryInterceptor();
		retryInterceptor.setApplicationContext(applicationContext);
		retryInterceptor.setRetryConfiguration(configuration);
		return retryInterceptor;
	}

	@Bean
	@ConditionalOnMissingBean(RetryExecutor.class)
	public PersistenceRetryExecutor defaultRetryExecutor(RetryConfiguration configuration, RetryFilterInvocation retryInvocationHandler) {
		PersistenceRetryExecutor persistenceRetryExecutor = new PersistenceRetryExecutor();
		persistenceRetryExecutor.setRetryConfiguration(configuration);
		persistenceRetryExecutor.setRetryFilterInvocation(retryInvocationHandler);
		return persistenceRetryExecutor;
	}

	@Bean
	@ConditionalOnMissingBean(RetryEventMulticaster.class)
	public RetryEventMulticaster retryEventMulticaster() {
		return new SimpleRetryEventMulticaster();
	}

	@Bean
	@ConditionalOnMissingBean(RetryListenerInitialize.class)
	public RetryListenerInitialize retryListenerInitialize(RetryEventMulticaster retryEventMulticaster) {
		RetryListenerInitialize retryListenerInitialize = new RetryListenerInitialize();
		retryListenerInitialize.setRetryEventMulticaster(retryEventMulticaster);
		return retryListenerInitialize;
	}

	@Bean
	@ConditionalOnMissingBean(SpringRetryFilterDiscover.class)
	public SpringRetryFilterDiscover springRetryFilterDiscover() {
		return new SpringRetryFilterDiscover();
	}

	@Bean
	@ConditionalOnMissingBean(RetryFilterRegister.class)
	public SimpleRetryFilterRegister simpleRetryFilterRegister(){
		return new SimpleRetryFilterRegister();
	}

	@Bean
	@ConditionalOnMissingBean(RetryFilterInvocationHandler.class)
	public DefaultRetryFilterInvocationHandler retryInvocationHandler(RetryFilterRegister simpleRetryFilterRegister) {
		DefaultRetryFilterInvocationHandler defaultRetryFilterInvocationHandler =  new DefaultRetryFilterInvocationHandler();
		defaultRetryFilterInvocationHandler.setRetryFilterRegister(simpleRetryFilterRegister);
		return defaultRetryFilterInvocationHandler;
	}

	@Bean
	@ConditionalOnMissingBean(RetryFilterRegisterHandler.class)
	public RetryFilterRegisterHandler retryFilterRegisterHandler(RetryFilterDiscover springRetryFilterDiscover,RetryFilterRegister simpleRetryFilterRegister){
		DefaultRetryFilterRegisterHandler defaultRetryFilterRegisterHandler = new DefaultRetryFilterRegisterHandler();
		defaultRetryFilterRegisterHandler.setRetryFilterRegister(simpleRetryFilterRegister);
		defaultRetryFilterRegisterHandler.setRetryFilterDiscover(springRetryFilterDiscover);
		return defaultRetryFilterRegisterHandler;
	}

	@Bean
	public ApplicationListener easyRetryApplicationListener(RetryFilterInvocationHandler retryFilterInvocationHandler,RetryFilterRegisterHandler retryFilterRegisterHandler){
		SpringEventApplicationListener springEventApplicationListener = new SpringEventApplicationListener();
		springEventApplicationListener.setRetryFilterRegisterHandler(retryFilterRegisterHandler);
		springEventApplicationListener.setRetryFilterInvocationHandler(retryFilterInvocationHandler);
		return springEventApplicationListener;
	}

	public abstract Integer getMaxRetryTimes();

}
