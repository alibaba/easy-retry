package com.alibaba.easyretry.mybatis.conifg;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Created by wuhao on 2021/2/21.
 */
@ConfigurationProperties(prefix = "spring.easyretry.mybatis")
@Data
public class MybatisProperties {

    private Integer maxRetryTimes = 5;

    private String namespace = "easy-retry";

}
