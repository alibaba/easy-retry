package com.alibaba.easyretry.core.filter;

import java.util.List;

import com.alibaba.easyretry.common.filter.RetryFilter;
import com.alibaba.easyretry.common.filter.RetryFilterDiscover;
import com.alibaba.easyretry.common.filter.RetryFilterRegister;
import com.alibaba.easyretry.common.filter.RetryFilterRegisterHandler;

import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @author Created by wuhao on 2021/4/9.
 */
public class DefaultRetryFilterRegisterHandler implements RetryFilterRegisterHandler {

	@Setter
	private RetryFilterDiscover retryFilterDiscover;

	@Setter
	private RetryFilterRegister retryFilterRegister;

	@Override
	public void handle() {
		List<RetryFilter> retryFilters = retryFilterDiscover.discoverAll();
		for (RetryFilter retryFilter : CollectionUtils.emptyIfNull(retryFilters)) {
			retryFilterRegister.register(retryFilter);
		}
	}

}
