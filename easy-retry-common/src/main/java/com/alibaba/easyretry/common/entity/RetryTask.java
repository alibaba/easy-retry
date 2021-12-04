package com.alibaba.easyretry.common.entity;

import java.util.Date;
import java.util.Map;

import com.alibaba.easyretry.common.constant.enums.RetryTaskStatusEnum;

import lombok.Data;

/**
 * 重试任务实体
 *
 * @author wuhao
 */
@Data
public class RetryTask {

	/**
	 * 主键id
	 */
	private Long id;

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
	 * 当重试失败时候执行的方法
	 */
	private String recoverMethod;


	/**
	 * 重试的结果判断
	 */
	private String resultCondition;

	/**
	 * 重试任务状态
	 */
	private RetryTaskStatusEnum status;

	/**
	 * 任务上的扩展字段
	 */
	private Map<String, String> extAttrs;

	/**
	 * 重试执行者方法参数
	 */
	private String argsStr;

	private Date gmtCreate;

	private Date gmtModified;
}
