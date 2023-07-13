package com.alibaba.easyretry.core;

import com.alibaba.easyretry.common.SCallable;
import com.alibaba.easyretry.common.constant.enums.RetryTypeEnum;
import com.alibaba.easyretry.common.retryer.Retryer;

/**
 * @author Created by zhangchi on 2023-07-12
 */
public class App {


	public static void main(String[] args) throws Throwable {
		SCallable<Object> task = ()->{
			method();
			return null;
		};

		Retryer<Object> retryer = new RetryerBuilder<>()
			.withRetryTimes(4).withRetryIntervalTime(500L).build(RetryTypeEnum.SYNC);
		retryer.call(task);
	}


	public static void method() {
		System.out.println("1111");
		int a = 1 / 0;
	}


}
