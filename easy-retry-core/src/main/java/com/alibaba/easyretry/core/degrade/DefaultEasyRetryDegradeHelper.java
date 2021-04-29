package com.alibaba.easyretry.core.degrade;

import com.alibaba.easyretry.common.RetryContext;

/**
 * @author Created by gejinfeng on 2021/4/29.
 */
public class DefaultEasyRetryDegradeHelper implements EasyRetryDegradeHelper{

	@Override
	public boolean degrade(RetryContext retryContext) {
		return false;
	}
}
