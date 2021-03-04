package com.alibaba.easyretry.memory;

import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.RetryContainer;
import com.alibaba.easyretry.common.RetryExecutor;
import com.alibaba.easyretry.common.access.RetrySerializerAccess;
import com.alibaba.easyretry.common.access.RetryStrategyAccess;
import com.alibaba.easyretry.common.access.RetryTaskAccess;
import com.alibaba.easyretry.common.entity.RetryTask;
import com.alibaba.easyretry.common.resolve.ExecutorSolver;
import com.alibaba.easyretry.common.strategy.StopStrategy;
import com.alibaba.easyretry.common.strategy.WaitStrategy;
import com.alibaba.easyretry.core.DefaultRetryExecutor;
import com.alibaba.easyretry.core.access.DefaultRetrySerializerAccess;
import com.alibaba.easyretry.core.access.MemoryRetryTaskAccess;
import com.alibaba.easyretry.core.container.SimpleRetryContainer;
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
                    public StopStrategy getStopStrategy(RetryTask retryTaskDomain) {
                        return defaultRetryStrategy;
                    }

                    @Override
                    public WaitStrategy getCurrentGlobalWaitStrategy() {
                        return defaultRetryStrategy;
                    }

                    @Override
                    public WaitStrategy getWaitStrategy(RetryTask retryTaskDomain) {
                        return defaultRetryStrategy;
                    }
                };
            }

            @Override
            public ExecutorSolver getExecutorSolver() {
                return executorName -> applicationContext.getBean(executorName);
            }

            @Override
            public Integer getMaxRetryTimes() {
                return easyRetryMemoryCompatibleProperties.getMaxRetryTimes();
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(RetryInterceptor.class)
    public RetryInterceptor retryInterceptor(RetryConfiguration configuration) {
        RetryInterceptor retryInterceptor = new RetryInterceptor();
        retryInterceptor.setApplicationContext(applicationContext);
        retryInterceptor.setRetryConfiguration(configuration);
        retryInterceptor.setNamespace(easyRetryMemoryCompatibleProperties.getNamespace());
        return retryInterceptor;
    }

    @Bean(initMethod = "start")
    public RetryContainer retryContainer(RetryConfiguration configuration, RetryExecutor defaultRetryExecutor) {
        log.warn("RetryConfiguration start");
        return new SimpleRetryContainer(configuration, easyRetryMemoryCompatibleProperties.getNamespace(),defaultRetryExecutor);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    @ConditionalOnMissingBean(RetryExecutor.class)
    public DefaultRetryExecutor defaultRetryExecutor(RetryConfiguration configuration) {
        DefaultRetryExecutor defaultRetryExecutor = new DefaultRetryExecutor();
        defaultRetryExecutor.setRetryConfiguration(configuration);
        return defaultRetryExecutor;
    }

}
