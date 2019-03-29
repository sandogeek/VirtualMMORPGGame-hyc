package com.mmorpg.mbdl.common.jsonserializer;

import com.baidu.bjf.remoting.protobuf.EnumReadable;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * EnumReadable枚举反序列化
 *
 * @author Sando Geek
 * @since v1.0 2019/2/22
 **/
public class EnumReadableDeserializer extends JsonDeserializer<EnumReadable> {
    private static Logger logger = LoggerFactory.getLogger(EnumReadableDeserializer.class);
    private static Table<Class<?>,Integer,EnumReadable> class2code2EnumReadable = HashBasedTable.create();
    @Override
    public EnumReadable deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String keyName = p.getCurrentName();
        int value = p.getValueAsInt();
        Field field;
        Class<?> type = null;
        try {
            field = p.getCurrentValue().getClass().getDeclaredField(keyName);
            type = field.getType();
            if (!type.isEnum()) {
                throw new RuntimeException("此反序列化器不能用在非枚举类上");
            }
            if (!class2code2EnumReadable.containsRow(type)) {
                Enum[] enumConstants = (Enum[]) type.getEnumConstants();
                for (Enum temp:
                        enumConstants) {
                    EnumReadable readable = (EnumReadable) temp;
                    class2code2EnumReadable.put(type,readable.value(),readable);
                    if (readable.value() == value) {
                        return readable;
                    }
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return class2code2EnumReadable.get(type,value);
    }
}
