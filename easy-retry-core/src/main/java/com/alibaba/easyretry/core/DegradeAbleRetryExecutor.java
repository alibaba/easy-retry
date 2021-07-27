package com.alibaba.easyretry.core;

import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.RetryExecutor;
import com.alibaba.easyretry.common.constant.enums.HandleResultEnum;
import com.alibaba.easyretry.core.degrade.EasyRetryDegradeHelper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Created by gejinfeng on 2021/4/29.
 */
@Slf4j
public class DegradeAbleRetryExecutor implements RetryExecutor {

	@Setter
	private RetryExecutor retryExecutor;

	@Setter
	private EasyRetryDegradeHelper easyRetryDegradeHelper;

	@Override
	public HandleResultEnum doExecute(RetryContext context) {
		if (easyRetryDegradeHelper.degrade(context)) {
			return HandleResultEnum.STOP;
		}
		return retryExecutor.doExecute(context);
	}
}
