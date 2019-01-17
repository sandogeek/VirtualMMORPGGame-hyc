package com.mmorpg.mbdl.business.common.orm;

import com.mmorpg.mbdl.business.common.util.JsonUtil;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * jpa中的json转换器
 *
 * @author Sando Geek
 * @since v1.0 2019/1/17
 **/
@Converter
public class JsonConverter implements AttributeConverter<Object,String> {
    private Class clazz;
    @Override
    public String convertToDatabaseColumn(Object attribute) {
        clazz = attribute.getClass();
        if (attribute == null) {
            return null;
        }
        return JsonUtil.object2String(attribute);
    }

    @Override
    public Object convertToEntityAttribute(String dbData) {
        return JsonUtil.string2Object(dbData, clazz);
    }
}
