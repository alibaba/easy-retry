package com.alibaba.easyretry.common.access;

import com.alibaba.easyretry.common.way.SyncRetryWay;

/**
 * @author zhangchi20 <zhangchi20@kuaishou.com>
 * Created on 2023-07-17
 */
public interface SyncRetryWayAccess<V> {

	/**
	 * 获取全局同步重试方式
	 */
	SyncRetryWay<V> getCurrentGlobalSyncRetryWay();

}
