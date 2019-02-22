package com.mmorpg.mbdl.business.common.jsonserializer;

import com.baidu.bjf.remoting.protobuf.EnumReadable;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * EnumReadable枚举反序列化
 *
 * @author Sando Geek
 * @since v1.0 2019/2/22
 **/
public class EnumReadableDeserializer extends JsonDeserializer<EnumReadable> {
    @Override
    public EnumReadable deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String keyName = p.getCurrentName();
        int value = p.getValueAsInt();
        Field field;
        try {
            field = p.getCurrentValue().getClass().getDeclaredField(keyName);
            Class<?> type = field.getType();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}
