package com.mmorpg.mbdl.business.common.orm;

import com.mmorpg.mbdl.framework.common.utils.JsonUtil;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * jpa中的json转换器
 * @PersistenceContext
 *     private EntityManager em;
 *
 * @author Sando Geek
 * @since v1.0 2019/1/17
 **/
@Converter
public class JsonConverter implements AttributeConverter<Object,String> {

    @Override
    public String convertToDatabaseColumn(Object attribute) {
        if (attribute == null) {
            return null;
        }
        return JsonUtil.object2String(attribute);
    }

    @Override
    public Object convertToEntityAttribute(String dbData) {
        // Type genericType = field.getGenericType();
        // JavaType javaType = JsonUtil.getTypeFactory().constructType(genericType);
        // return JsonUtil.string2Object(dbData, javaType);
        return JsonUtil.string2Object(dbData, Object.class);
    }
}
