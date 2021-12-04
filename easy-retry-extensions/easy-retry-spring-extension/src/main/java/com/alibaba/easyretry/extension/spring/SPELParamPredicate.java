package com.alibaba.easyretry.extension.spring;

import java.lang.reflect.Method;

import com.alibaba.easyretry.common.predicate.EasyRetryPredicate;

import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author Created by wuhao on 2021/3/18.
 */
@Data
public class SPELParamPredicate implements EasyRetryPredicate<Object[], String> {

	private static DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

	private String bizIdCondition;

	private Method method;

	public SPELParamPredicate(String bizIdCondition, Method method) {
		this.bizIdCondition = bizIdCondition;
		this.method = method;
	}

	public SPELParamPredicate() {
	}

	@Override
	public String apply(Object[] params) {
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		String[] paramNameArr = discoverer.getParameterNames(method);
		if (ArrayUtils.isEmpty(paramNameArr)) {
			return null;
		}
		for (int i = 0; i < paramNameArr.length; i++) {
			context.setVariable(paramNameArr[i], params[i]);
		}
		return parser.parseExpression(bizIdCondition).getValue(context, String.class);
	}
}
