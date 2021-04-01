package com.alibaba.easyretry.extension.mybatis.po;

import com.alibaba.easyretry.common.constant.enums.RetryTaskStatusEnum;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Created by wuhao on 2020/11/8.
 */
@Data
@Accessors(chain = true)
public class RetryTaskPO {

	/**
	 * 主键id
	 */
	private Long id;

	/**
	 * 分片id
	 */
	private String sharding;

	/**
	 * 业务信息
	 */
	private String bizId;

	/**
	 * 执行者名称
	 */
	private String executorName;

	/**
	 * 执行者方法
	 */
	private String executorMethodName;

	/**
	 * @see RetryTaskStatusEnum
	 */
	private Integer retryStatus;

	private String argsStr;

	private Date gmtCreate;

	private Date gmtModified;

	private String extAttrs;
}
