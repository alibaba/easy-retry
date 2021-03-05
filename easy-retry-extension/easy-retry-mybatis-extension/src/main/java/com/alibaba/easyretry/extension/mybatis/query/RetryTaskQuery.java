package com.alibaba.easyretry.extension.mybatis.query;

import java.util.List;

import lombok.Data;

/**
 * @author Created by wuhao on 2020/11/8.
 */
@Data
public class RetryTaskQuery {

    private Long lastId;

    private List<Integer> retryStatus;

    private String sharding;

}
