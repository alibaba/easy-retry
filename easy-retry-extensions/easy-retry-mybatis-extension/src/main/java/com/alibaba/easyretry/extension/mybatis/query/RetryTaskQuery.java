package com.alibaba.easyretry.extension.mybatis.query;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Created by wuhao on 2020/11/8.
 */
@Data
@Accessors(chain = true)
public class RetryTaskQuery {

	private Long lastId;

	private List<Integer> retryStatus;

	private String sharding;
}
