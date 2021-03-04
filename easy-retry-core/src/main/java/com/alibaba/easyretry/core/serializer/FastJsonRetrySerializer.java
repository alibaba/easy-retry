package com.alibaba.easyretry.core.serializer;

import java.util.stream.Stream;

import com.alibaba.easyretry.common.serializer.ArgDeSerializerInfo;
import com.alibaba.easyretry.common.serializer.ArgSerializerInfo;
import com.alibaba.easyretry.common.serializer.RetryArgSerializer;
import com.alibaba.fastjson.JSON;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

/**
 */
public class FastJsonRetrySerializer implements RetryArgSerializer {

    public final static String SPLIT = "||";

    public final static String INNER_SPLIT = "&&";


    @Override
    public String serialize(ArgSerializerInfo argSerializerInfo) {
        StringBuilder sb = new StringBuilder();
        Stream.of(argSerializerInfo.getArgs()).forEach((arg)-> sb.append(JSON.toJSONString(arg)).append(INNER_SPLIT).append(arg.getClass().getName()).append(SPLIT));
        if (sb.length() >= SPLIT.length()) {
            return sb.subSequence(0, sb.length() - SPLIT.length()).toString();
        } else {
            return null;
        }
    }

    @Override
    public Object[] deSerialize(ArgDeSerializerInfo argDeSerializerInfo) {
        String[] strs = StringUtils.split(argDeSerializerInfo.getArgsStr(), SPLIT);
        return Stream.of(strs).map((str)->{
            String[] inner = str.split(INNER_SPLIT);
            try {
                return JSON.parseObject(inner[0], ClassUtils.getClass(inner[1]));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).toArray();
    }
}
