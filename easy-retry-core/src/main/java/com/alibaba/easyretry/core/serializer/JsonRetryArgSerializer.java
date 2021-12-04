package com.alibaba.easyretry.core.serializer;

import java.util.stream.Stream;

import com.alibaba.easyretry.common.serializer.ArgSerializerInfo;
import com.alibaba.easyretry.common.serializer.RetryArgSerializer;
import com.alibaba.easyretry.core.utils.RetryJsonUtils;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public class JsonRetryArgSerializer implements RetryArgSerializer {

	public static final String SPLIT = "||";

	public static final String INNER_SPLIT = "&&";

	@Override
	public String serialize(ArgSerializerInfo argSerializerInfo) {
		StringBuilder sb = new StringBuilder();
		Stream.of(argSerializerInfo.getArgs()).forEach(
			(arg) -> sb.append(RetryJsonUtils.toJSONString(arg)).append(INNER_SPLIT)
				.append(arg.getClass().getName()).append(SPLIT));
		if (sb.length() >= SPLIT.length()) {
			return sb.subSequence(0, sb.length() - SPLIT.length()).toString();
		} else {
			return null;
		}
	}

	@Override
	public ArgSerializerInfo deSerialize(String argsStr) {
		String[] strs = StringUtils.split(argsStr, SPLIT);
		Object[] arg = Stream.of(strs)
			.map((str) -> {
				String[] inner = str.split(INNER_SPLIT);
				try {
					return RetryJsonUtils.parseObject(inner[0], ClassUtils.getClass(inner[1]));
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			})
			.toArray();
		ArgSerializerInfo argSerializerInfo = new ArgSerializerInfo();
		argSerializerInfo.setArgs(arg);
		return argSerializerInfo;
	}
}
