package com.alibaba.easyretry.core.degrade;

import com.alibaba.easyretry.common.RetryContext;

/**
 * @author Created by gejinfeng on 2021/4/29.
 */
public interface EasyRetryDegradeHelper {

	/**
	 * 是否降级
	 *
	 * @param retryContext retryContext
	 * @return degrade
	 */
	boolean degrade(RetryContext retryContext);

}
