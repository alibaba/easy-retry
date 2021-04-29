package com.alibaba.easyretry.memory;

import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.RetryContainer;
import com.alibaba.easyretry.common.RetryExecutor;
import com.alibaba.easyretry.common.access.RetrySerializerAccess;
import com.alibaba.easyretry.common.access.RetryStrategyAccess;
import com.alibaba.easyretry.common.access.RetryTaskAccess;
import com.alibaba.easyretry.common.event.RetryEventMulticaster;
import com.alibaba.easyretry.common.filter.RetryInvocationHandler;
import com.alibaba.easyretry.common.resolve.ExecutorSolver;
import com.alibaba.easyretry.common.serializer.ResultPredicateSerializer;
import com.alibaba.easyretry.common.strategy.StopStrategy;
import com.alibaba.easyretry.common.strategy.WaitStrategy;
import com.alibaba.easyretry.core.DegradeAbleRetryExecutor;
import com.alibaba.easyretry.core.PersistenceRetryExecutor;
import com.alibaba.easyretry.core.access.DefaultRetrySerializerAccess;
import com.alibaba.easyretry.core.access.MemoryRetryTaskAccess;
import com.alibaba.easyretry.core.container.SimpleRetryContainer;
import com.alibaba.easyretry.core.degrade.DefaultEasyRetryDegradeHelper;
import com.alibaba.easyretry.core.degrade.EasyRetryDegradeHelper;
import com.alibaba.easyretry.core.event.SimpleRetryEventMulticaster;
import com.alibaba.easyretry.core.filter.DefaultRetryInvocationHandler;
import com.alibaba.easyretry.core.serializer.HessianResultPredicateSerializer;
import com.alibaba.easyretry.core.strategy.DefaultRetryStrategy;
import com.alibaba.easyretry.extension.spring.aop.RetryInterceptor;
import com.alibaba.easyretry.memory.config.EasyRetryMemoryCompatibleProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Created by wuhao on 2021/2/19.
 */
@Configuration
@Slf4j
@EnableConfigurationProperties(EasyRetryMemoryCompatibleProperties.class)
@ConditionalOnProperty(name = "spring.easyretry.memory.enabled", havingValue = "true")
public class MemoryAutoConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Autowired
	private EasyRetryMemoryCompatibleProperties easyRetryMemoryCompatibleProperties;

	@Bean
	@ConditionalOnMissingBean(MemoryRetryTaskAccess.class)
	public MemoryRetryTaskAccess memoryRetryTaskAccess() {
		return new MemoryRetryTaskAccess();
	}

	@Bean
	@ConditionalOnMissingBean(RetryConfiguration.class)
	public RetryConfiguration configuration(RetryTaskAccess amobaARetryTaskAccess) {
		DefaultRetryStrategy defaultRetryStrategy = new DefaultRetryStrategy();
		ResultPredicateSerializer resultPredicateSerializer = new HessianResultPredicateSerializer();
		return new RetryConfiguration() {
			@Override
			public RetryTaskAccess getRetryTaskAccess() {
				return amobaARetryTaskAccess;
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
				return resultPredicateSerializer;
			}

			@Override
			public Integer getMaxRetryTimes() {
				return easyRetryMemoryCompatibleProperties.getMaxRetryTimes();
			}

			@Override
			public RetryEventMulticaster getRetryEventMulticaster() {
				return new SimpleRetryEventMulticaster();
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

	@Bean(initMethod = "start")
	public RetryContainer retryContainer(
		RetryConfiguration configuration, RetryExecutor defaultRetryExecutor) {
		log.warn("RetryConfiguration start");
		return new SimpleRetryContainer(
			configuration,
			defaultRetryExecutor);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Bean
	@ConditionalOnMissingBean(RetryInvocationHandler.class)
	public RetryInvocationHandler retryInvocationHandler() {
		DefaultRetryInvocationHandler retryInvocationHandler = new DefaultRetryInvocationHandler();
		retryInvocationHandler.init();
		return retryInvocationHandler;
	}

	@Bean
	@ConditionalOnMissingBean(EasyRetryDegradeHelper.class)
	public EasyRetryDegradeHelper defaultEasyRetryDegradeHelper() {
		EasyRetryDegradeHelper defaultEasyRetryDegradeHelper = new DefaultEasyRetryDegradeHelper();
		return defaultEasyRetryDegradeHelper;
	}

	@Bean
	@ConditionalOnMissingBean(RetryExecutor.class)
	@ConditionalOnProperty(name = "spring.easyretry.degrade.enabled", havingValue = "true", matchIfMissing = false)
	public DegradeAbleRetryExecutor defaultRetryExecutor(RetryConfiguration configuration,
														 RetryInvocationHandler retryInvocationHandler,
														 EasyRetryDegradeHelper defaultEasyRetryDegradeHelper) {
		PersistenceRetryExecutor persistenceRetryExecutor = new PersistenceRetryExecutor();
		persistenceRetryExecutor.setRetryConfiguration(configuration);
		persistenceRetryExecutor.setRetryInvocationHandler(retryInvocationHandler);

		DegradeAbleRetryExecutor degradeAbleRetryExecutor = new DegradeAbleRetryExecutor();
		degradeAbleRetryExecutor.setRetryExecutor(persistenceRetryExecutor);
		degradeAbleRetryExecutor.setEasyRetryDegradeHelper(defaultEasyRetryDegradeHelper);
		return degradeAbleRetryExecutor;
	}

	@Bean
	@ConditionalOnMissingBean(RetryExecutor.class)
	@ConditionalOnProperty(name = "spring.easyretry.degrade.enabled", havingValue = "false", matchIfMissing = true)
	public PersistenceRetryExecutor defaultRetryExecutor(RetryConfiguration configuration,
		RetryInvocationHandler retryInvocationHandler) {
		PersistenceRetryExecutor persistenceRetryExecutor = new PersistenceRetryExecutor();
		persistenceRetryExecutor.setRetryConfiguration(configuration);
		persistenceRetryExecutor.setRetryInvocationHandler(retryInvocationHandler);
		return persistenceRetryExecutor;
	}
}
