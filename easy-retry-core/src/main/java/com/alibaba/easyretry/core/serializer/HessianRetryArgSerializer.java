package com.alibaba.easyretry.core.serializer;

/**
 * @author Created by wuhao on 2021/2/22.
 */

import com.alibaba.easyretry.common.serializer.ArgSerializerInfo;
import com.alibaba.easyretry.common.serializer.RetryArgSerializer;
import com.alibaba.easyretry.core.utils.HessianSerializerUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HessianRetryArgSerializer implements RetryArgSerializer {

	@Override
	public String serialize(ArgSerializerInfo argSerializerInfo) {
		Object[] args = argSerializerInfo.getArgs();
		if (args.length == 0) {
			throw new IllegalStateException("No args found");
		}
		return HessianSerializerUtils.serialize(args);
	}

	@Override
	public ArgSerializerInfo deSerialize(String argStr) {
		Object[] result = HessianSerializerUtils.deSerialize(argStr, Object[].class);
		ArgSerializerInfo argSerializerInfo = new ArgSerializerInfo();
		argSerializerInfo.setArgs(result);
		return argSerializerInfo;
	}
}
