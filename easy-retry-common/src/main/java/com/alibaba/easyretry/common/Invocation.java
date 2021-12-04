package com.alibaba.easyretry.common;

import java.lang.reflect.Method;

import lombok.Data;


@Data
public class Invocation {

	private Object executor;

	private Method method;

	private Object[] args;

}
