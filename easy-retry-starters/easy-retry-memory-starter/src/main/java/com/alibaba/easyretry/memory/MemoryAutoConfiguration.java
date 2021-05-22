package com.alibaba.easyretry.memory;

import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.RetryContainer;
import com.alibaba.easyretry.common.RetryExecutor;
import com.alibaba.easyretry.core.access.MemoryRetryTaskAccess;
import com.alibaba.easyretry.core.container.SimpleRetryContainer;
import com.alibaba.easyretry.memory.config.EasyRetryMemoryCompatibleProperties;
import com.alibaba.easyretry.starter.common.CommonAutoConfiguration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Created by wuhao on 2021/2/19.
 */
@Configuration
@Slf4j
@EnableConfigurationProperties(EasyRetryMemoryCompatibleProperties.class)
@ConditionalOnProperty(name = "spring.easyretry.memory.enabled", havingValue = "true")
public class MemoryAutoConfiguration extends CommonAutoConfiguration {

	@Autowired
	private EasyRetryMemoryCompatibleProperties easyRetryMemoryCompatibleProperties;

	@Bean
	@ConditionalOnMissingBean(MemoryRetryTaskAccess.class)
	public MemoryRetryTaskAccess retryTaskAccess() {
		return new MemoryRetryTaskAccess();
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
	public Integer getMaxRetryTimes() {
		return easyRetryMemoryCompatibleProperties.getMaxRetryTimes();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
