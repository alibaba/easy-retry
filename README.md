# Easy-Retry
一种存储介质可扩展的持久化重试方案
![img](img/readme/arch.jpg)

### Getting started
#### Memory Retry
增加pom依赖

```
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>easy-retry-memory-starter</artifactId>
    <version>1.0.0.RC</version>
</dependency>
```


在application.properties增加配置

`spring.easyretry.memory.enabled = true`

在需要重试的方法上增加@EasyRetryable注解
```
public class MemoryUserService {
    @EasyRetryable
    public User getUserById(Long userId){
        return new User();
    }
}
```

#### Mybatis Retry
```
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>easy-retry-mybatis-starter</artifactId>
    <version>1.0.0.RC</version>
</dependency>
```


在application.properties增加配置

`spring.easyretry.mybatis.enabled = true`

新增持久化表
```
CREATE TABLE `easy_retry_task` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `gmt_create` datetime NOT NULL COMMENT '创建时间',
    `gmt_modified` datetime NOT NULL COMMENT '修改时间',
    `sharding` varchar(64) NULL COMMENT '1',
    `biz_id` varchar(64) NULL COMMENT '2',
    `executor_name` varchar(512) NULL COMMENT '3',
    `executor_method_name` varchar(512) NULL COMMENT '4',
    `retry_status` tinyint NULL COMMENT '5',
    `args_str` varchar(3000) NULL COMMENT '6',
    `ext_attrs` varchar(3000) NULL COMMENT '7',
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET=utf8 AUTO_INCREMENT=0 COMMENT='easy_retry_task';
```

在需要重试的方法上增加@EasyRetryable注解
```
public class MybatisUserService {
    @EasyRetryable
    public User getUserById(Long userId){
        return new User();
    }
}
```

###Built With
• JDK1.8

• Spring Framework5+

• Spring Boot2.4+

• Maven3.0
