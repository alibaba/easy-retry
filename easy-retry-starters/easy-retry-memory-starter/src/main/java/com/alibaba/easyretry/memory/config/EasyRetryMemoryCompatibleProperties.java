package com.alibaba.easyretry.memory.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Created by wuhao on 2021/2/21.
 */
@ConfigurationProperties(prefix = "spring.easyretry.memory")
@Data
public class EasyRetryMemoryCompatibleProperties {

    private Integer maxRetryTimes = 5;

    private String namespace = "easy-retry";

}
