package com.alibaba.easyretry.mybatis;

import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.RetryContainer;
import com.alibaba.easyretry.common.RetryExecutor;
import com.alibaba.easyretry.common.access.RetrySerializerAccess;
import com.alibaba.easyretry.common.access.RetryStrategyAccess;
import com.alibaba.easyretry.common.access.RetryTaskAccess;
import com.alibaba.easyretry.common.event.RetryEventMulticaster;
import com.alibaba.easyretry.common.event.RetryListener;
import com.alibaba.easyretry.common.filter.RetryInvocationHandler;
import com.alibaba.easyretry.common.resolve.ExecutorSolver;
import com.alibaba.easyretry.common.serializer.ResultPredicateSerializer;
import com.alibaba.easyretry.common.strategy.StopStrategy;
import com.alibaba.easyretry.common.strategy.WaitStrategy;
import com.alibaba.easyretry.core.PersistenceRetryExecutor;
import com.alibaba.easyretry.core.access.DefaultRetrySerializerAccess;
import com.alibaba.easyretry.core.container.SimpleRetryContainer;
import com.alibaba.easyretry.core.event.SimpleRetryEventMulticaster;
import com.alibaba.easyretry.core.filter.DefaultRetryInvocationHandler;
import com.alibaba.easyretry.core.serializer.HessianResultPredicateSerializer;
import com.alibaba.easyretry.core.strategy.DefaultRetryStrategy;
import com.alibaba.easyretry.extension.mybatis.access.MybatisRetryTaskAccess;
import com.alibaba.easyretry.extension.mybatis.dao.RetryTaskDAO;
import com.alibaba.easyretry.extension.mybatis.dao.RetryTaskDAOImpl;
import com.alibaba.easyretry.extension.spring.aop.RetryInterceptor;
import com.alibaba.easyretry.mybatis.conifg.EasyRetryMybatisProperties;
import com.google.common.collect.Lists;
import java.util.Map;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * @author Created by wuhao on 2021/2/19.
 */
@Configuration
@Slf4j
@EnableConfigurationProperties(EasyRetryMybatisProperties.class)
@ConditionalOnProperty(name = "spring.easyretry.mybatis.enabled", matchIfMissing = true)
public class MybatisAutoConfiguration implements ApplicationContextAware,
	SmartInitializingSingleton {

	private ApplicationContext applicationContext;

	@Autowired
	private EasyRetryMybatisProperties easyRetryMybatisProperties;

	@Value("classpath:/dal/easyretry/easy-mybatis-config.xml")
	private Resource easyRetryMybatisResouse;

	@Bean("easyRetrySqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(
		@Qualifier("easyRetryMybatisDataSource") DataSource easyRetryMybatisDataSource)
		throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(easyRetryMybatisDataSource);
		sqlSessionFactoryBean.setConfigLocation(easyRetryMybatisResouse);
		return sqlSessionFactoryBean.getObject();
	}

	@Bean
	public RetryTaskDAO retryTaskDAO(
		@Qualifier("easyRetrySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new RetryTaskDAOImpl(sqlSessionFactory);
//		RetryTaskDAOImpl retryTaskDAO = new RetryTaskDAOImpl();
//		retryTaskDAO.setSqlSessionFactory(sqlSessionFactory);
//		retryTaskDAO.init();
//		return retryTaskDAO;
	}

	@Bean
	public RetryTaskAccess mybatisRetryTaskAccess(RetryTaskDAO retryTaskDAO) {
		return new MybatisRetryTaskAccess(retryTaskDAO);
	}

	@Bean
	@ConditionalOnMissingBean(RetryConfiguration.class)
	public RetryConfiguration configuration(RetryTaskAccess mybatisRetryTaskAccess,
		RetryEventMulticaster retryEventMulticaster) {
		DefaultRetryStrategy defaultRetryStrategy = new DefaultRetryStrategy();
		ResultPredicateSerializer resultPredicateSerializer = new HessianResultPredicateSerializer();
		return new RetryConfiguration() {
			@Override
			public RetryTaskAccess getRetryTaskAccess() {
				return mybatisRetryTaskAccess;
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
				return easyRetryMybatisProperties.getMaxRetryTimes();
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

	@Bean(initMethod = "start")
	public RetryContainer retryContainer(
		RetryConfiguration configuration, RetryExecutor defaultRetryExecutor) {
		log.warn("RetryConfiguration start");
		return new SimpleRetryContainer(
			configuration, defaultRetryExecutor);
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
	@ConditionalOnMissingBean(RetryEventMulticaster.class)
	public RetryEventMulticaster retryEventMulticaster() {
		SimpleRetryEventMulticaster simpleRetryEventMulticaster = new SimpleRetryEventMulticaster();
		return simpleRetryEventMulticaster;
	}


	@Bean
	@ConditionalOnMissingBean(RetryExecutor.class)
	public PersistenceRetryExecutor defaultRetryExecutor(RetryConfiguration configuration,
		RetryInvocationHandler retryInvocationHandler) {
		PersistenceRetryExecutor persistenceRetryExecutor = new PersistenceRetryExecutor();
		persistenceRetryExecutor.setRetryConfiguration(configuration);
		persistenceRetryExecutor.setRetryInvocationHandler(retryInvocationHandler);
		return persistenceRetryExecutor;
	}

	@Override
	public void afterSingletonsInstantiated() {
		Map<String, RetryListener> map = applicationContext.getBeansOfType(RetryListener.class);
		if (MapUtils.isNotEmpty(map)) {
			SimpleRetryEventMulticaster.setListenerCaches(Lists.newArrayList(map.values()));
		} else {
			SimpleRetryEventMulticaster.setListenerCaches(Lists.newArrayList());
		}
	}
}
