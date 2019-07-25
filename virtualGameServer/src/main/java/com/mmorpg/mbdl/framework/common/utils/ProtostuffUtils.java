package com.mmorpg.mbdl.framework.common.utils;

/**
 * protostuff工具
 *
 * @author Sando Geek
 * @since v1.0 2019/7/22
 **/

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProtostuffUtils {
    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    private static <T> Schema<T> getSchema(Class<T> clazz) {
        return (Schema<T>) cachedSchema.computeIfAbsent(clazz, RuntimeSchema::getSchema);
    }

    /**
     * 将对象序列化
     * @param obj 对象
     * @return
     */
    public static <T> byte[] serialize(T obj) {
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(clazz);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> int writeListTo(OutputStream out, List<T> messages) throws IOException {
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        Schema<T> schema = (Schema<T>) getSchema(messages.get(0).getClass());
        return ProtostuffIOUtil.writeListTo(out, messages, schema, buffer);
    }

    public static <T> List<T> parseListFrom(final InputStream in, Class<T> clz) throws IOException {
        return ProtostuffIOUtil.parseListFrom(in, getSchema(clz));
    }

    /**
     * 将字节数组数据反序列化
     * @param data 字节数组
     * @param clazz 对象
     * @return
     */
    public static <T> T deserialize(byte[] data, Class<T> clazz) {
        try {
            T obj = clazz.newInstance();
            Schema<T> schema = getSchema(clazz);
            ProtostuffIOUtil.mergeFrom(data, obj, schema);
            return obj;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}

