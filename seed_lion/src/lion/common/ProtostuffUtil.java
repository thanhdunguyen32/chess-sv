package lion.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

public class ProtostuffUtil {

	private static final Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<Class<?>, Schema<?>>();

	private static <T> Schema<T> getSchema(Class<T> clazz) {
		@SuppressWarnings("unchecked")
		Schema<T> schema = (Schema<T>) cachedSchema.get(clazz);
		if (schema == null) {
			schema = RuntimeSchema.getSchema(clazz);
			if (schema != null) {
				cachedSchema.put(clazz, schema);
			}
		}
		return schema;
	}

	/**
	 * 序列化
	 *
	 * @param obj
	 * @return
	 */
	public static <T> byte[] serialize(T obj) {
		if (obj == null) {
			return null;
		}
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) obj.getClass();
		LinkedBuffer buffer = LionGlobalBuffer.getProtoBuffer();
		try {
			Schema<T> schema = getSchema(clazz);
			return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		} finally {
			buffer.clear();
		}
	}

	/**
	 * 反序列化
	 *
	 * @param data
	 * @param clazz
	 * @return
	 */
	public static <T> T deserialize(byte[] data, Class<T> clazz) {
		if (data == null || data.length == 0) {
			return null;
		}
		try {
			T obj = clazz.newInstance();
			Schema<T> schema = getSchema(clazz);
			ProtostuffIOUtil.mergeFrom(data, obj, schema);
			return obj;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public static <T> T deserialize(byte[] data, T objInstance) {
		if (data == null || data.length == 0) {
			return null;
		}
		try {
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) objInstance.getClass();
			Schema<T> schema = getSchema(clazz);
			ProtostuffIOUtil.mergeFrom(data, objInstance, schema);
			return objInstance;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

}
