package com.alibaba.easyretry.core.event;

import com.alibaba.easyretry.common.event.RetryEvent;
import com.alibaba.easyretry.common.event.RetryEventMulticaster;
import com.alibaba.easyretry.common.event.RetryListener;
import com.google.common.collect.Lists;
import java.util.List;
import lombok.Setter;

/**
 * @author Created by wuhao on 2021/3/26.
 */
public class SimpleRetryEventMulticaster implements RetryEventMulticaster {

	@Setter
	private List<RetryListener> listenerCaches = Lists.newArrayList();


	@Override
	public void register(RetryListener listener) {
		listenerCaches.add(listener);
	}

	@Override
	public void multicast(RetryEvent retryEvent) {
		listenerCaches.forEach((retryListener) -> retryListener.onRetryEvent(retryEvent));
	}
}
