package com.mmorpg.mbdl.bussiness.common.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

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
            throw new RuntimeException("",e);
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
            throw new RuntimeException("",e);
        }
    }
}
