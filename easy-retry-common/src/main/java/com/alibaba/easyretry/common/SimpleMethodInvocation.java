package com.alibaba.easyretry.common;

import java.lang.reflect.Method;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Setter;

/**
 * @author Created by wuhao on 2021/3/29.
 */
@AllArgsConstructor
public class SimpleMethodInvocation implements Invocation {

	@Setter
	private Object executor;

	@Setter
	private Method method;

	@Setter
	private Object[] args;

	@Override
	public Object invoke() throws Throwable {
		return method.invoke(executor, args);
	}

	@Override
	public String toString() {
		return "[Invocation] executor is " + executor.getClass().getName() + " method is " + method
			.getName() + " args is " + Arrays.toString(args);

	}
}
