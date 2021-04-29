package com.alibaba.easyretry.core.serializer;

import com.alibaba.easyretry.common.AbstractResultPredicate;
import com.alibaba.easyretry.common.serializer.ResultPredicateSerializer;
import com.alibaba.easyretry.core.utils.HessianSerializerUtils;

/**
 * @author Created by wuhao on 2021/3/18.
 */
public class HessianResultPredicateSerializer implements ResultPredicateSerializer {

	@Override
	public String serialize(AbstractResultPredicate serializeInfo) {
		return HessianSerializerUtils.serialize(serializeInfo);
	}

	@Override
	public AbstractResultPredicate deSerialize(String infoStr) {
		return HessianSerializerUtils.deSerialize(infoStr, AbstractResultPredicate.class);
	}
}
