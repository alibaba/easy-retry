package com.alibaba.easyretry.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

/**
 * @author Created by wuhao on 2021/3/18.
 */
public class HessianSerializerUtils {

	public static <T> String serialize(T t) {
		try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			HessianOutput ho = new HessianOutput(os);
			ho.writeObject(t);
			return Base64.getEncoder().encodeToString(os.toByteArray());
		} catch (IOException e) {
			throw new IllegalStateException("HessianSerializationConverter.serialize failed.", e);
		}
	}

	public static <T> T deSerialize(String str, Class<T> tClass) {
		byte[] convertBytes = Base64.getDecoder().decode(str);
		try (ByteArrayInputStream is = new ByteArrayInputStream(convertBytes)) {
			HessianInput hi = new HessianInput(is);
			return (T)hi.readObject(tClass);
		} catch (IOException e) {
			throw new IllegalStateException("HessianSerializationConverter.deSerialize failed.", e);
		}
	}

}
