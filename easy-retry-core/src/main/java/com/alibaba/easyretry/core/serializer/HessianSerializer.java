package com.alibaba.easyretry.core.serializer;

/**
 * @author Created by wuhao on 2021/2/22.
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import com.alibaba.easyretry.common.serializer.ArgDeSerializerInfo;
import com.alibaba.easyretry.common.serializer.ArgSerializerInfo;
import com.alibaba.easyretry.common.serializer.RetryArgSerializer;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HessianSerializer implements RetryArgSerializer {

    @Override
    public String serialize(ArgSerializerInfo argSerializerInfo) {
        Object[] args = argSerializerInfo.getArgs();
        if (args.length == 0) {
            throw new IllegalStateException("No args found");
        }
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            HessianOutput ho = new HessianOutput(os);
            ho.writeObject(args);
            return Base64.getEncoder().encodeToString(os.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException("HessianSerializationConverter.serialize failed.", e);
        }
    }

    @Override
    public Object[] deSerialize(ArgDeSerializerInfo argDeSerializerInfo) {
        byte[] convertBytes = Base64.getDecoder().decode(argDeSerializerInfo.getArgsStr());
        try (ByteArrayInputStream is = new ByteArrayInputStream(convertBytes)) {
            HessianInput hi = new HessianInput(is);
            return (Object[])hi.readObject(Object[].class);
        } catch (IOException e) {
            throw new IllegalStateException("HessianSerializationConverter.deSerialize failed.", e);
        }
    }
}

