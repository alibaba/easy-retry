package com.alibaba.easyretry.extension.spring.aop;

import com.alibaba.easyretry.common.ResultPredicate;
import lombok.Data;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author Created by wuhao on 2021/3/18.
 */
@Data
public class SPELResultPredicate<R> implements ResultPredicate<R> {

	private String resultCondition;

	public SPELResultPredicate(String resultCondition) {
		this.resultCondition = resultCondition;
	}

	public SPELResultPredicate() {
	}


	@Override
	public boolean apply(R result) {
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariable("result", result);
		return parser.parseExpression(resultCondition).getValue(context, Boolean.class);
	}

}
