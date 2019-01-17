package com.mmorpg.mbdl.business.common.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * 序列化反序列化工具
 *
 * @author Sando Geek
 * @since v1.0 2019/1/2
 **/
public class JsonUtil {
    private static ObjectMapper mapper;
    static  {
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        // 在序列化时忽略值为 null 的属性
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 忽略值为默认值的属性
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_DEFAULT);
        // 不自动检测getter
        mapper.configure(MapperFeature.AUTO_DETECT_GETTERS, false);
        // 不自动检测is getter
        mapper.configure(MapperFeature.AUTO_DETECT_IS_GETTERS, false);
    }

    /**
     * 反序列化
     * @param json 待反序列化的内容
     * @param valueType 值类型
     * @param <T>
     * @return
     */
    public static <T> T string2Object(String json,Class<T> valueType) {
        try {
            return mapper.readValue(json,valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T string2Object(String json,JavaType valueType) {
        try {
            return mapper.readValue(json,valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T inputStream2Object(InputStream src, JavaType valueType){
        try {
            return mapper.readValue(src,valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 序列化
     * @param value 待序列化的对象
     * @return
     */
    public static String object2String(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static TypeFactory getTypeFactory() {
        return mapper.getTypeFactory();
    }

    public static CollectionType constructCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass) {
        return mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    }
}
