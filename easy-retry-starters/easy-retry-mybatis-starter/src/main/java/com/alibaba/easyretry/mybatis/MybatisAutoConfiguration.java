package com.alibaba.easyretry.mybatis;

import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.RetryContainer;
import com.alibaba.easyretry.common.RetryExecutor;
import com.alibaba.easyretry.common.access.RetryTaskAccess;
import com.alibaba.easyretry.core.container.SimpleRetryContainer;
import com.alibaba.easyretry.extension.mybatis.access.MybatisRetryTaskAccess;
import com.alibaba.easyretry.extension.mybatis.dao.RetryTaskDAO;
import com.alibaba.easyretry.extension.mybatis.dao.RetryTaskDAOImpl;
import com.alibaba.easyretry.mybatis.conifg.EasyRetryMybatisProperties;
import com.alibaba.easyretry.starter.common.CommonAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;

/**
 * @author Created by wuhao on 2021/2/19.
 */
@Configuration
@Slf4j
@EnableConfigurationProperties(EasyRetryMybatisProperties.class)
@ConditionalOnProperty(name = "spring.easyretry.mybatis.enabled", matchIfMissing = true)
public class MybatisAutoConfiguration extends CommonAutoConfiguration {

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

	@Bean(initMethod = "start")
	public RetryContainer retryContainer(
		RetryConfiguration configuration, RetryExecutor defaultRetryExecutor) {
		log.warn("RetryConfiguration start");
		return new SimpleRetryContainer(
			configuration, defaultRetryExecutor);
	}

	@Bean
	public RetryTaskDAO retryTaskDAO(
		@Qualifier("easyRetrySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new RetryTaskDAOImpl(sqlSessionFactory);
	}

	@Bean
	public RetryTaskAccess retryTaskAccess(RetryTaskDAO retryTaskDAO) {
		return new MybatisRetryTaskAccess(retryTaskDAO);
	}

	@Override
	public Integer getMaxRetryTimes() {
		return easyRetryMybatisProperties.getMaxRetryTimes();
	}
}
