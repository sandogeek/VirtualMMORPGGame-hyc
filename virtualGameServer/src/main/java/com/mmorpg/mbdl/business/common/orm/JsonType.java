package com.mmorpg.mbdl.business.common.orm;


import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.mmorpg.mbdl.business.common.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.springframework.core.ResolvableType;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义hibernate字段类型
 * 注意：对于集合类型，仅支持{@code Map<K,V>及map的子类型，Collection<E>，其中K,V不能是map,E不能是Collection及其子类，
 * K,V中不能包含Map或者Collection的子类，如有需要，要变成字符串，再自行转换}
 *
 * @author Sando Geek
 * @since v1.0 2019/1/2
 **/
public class JsonType implements UserType {

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.CLOB};
    }

    @Override
    public Class returnedClass() {
        return Object.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return ObjectUtils.nullSafeEquals(x,y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return ObjectUtils.nullSafeHashCode(x);
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        String json = rs.getString(names[0]);
        if (StringUtils.isEmpty(json)){
            return null;
        }
        String columnName = rs.getMetaData().getColumnName(rs.findColumn(names[0]));
        String entityJson = "{\""+columnName+ "\":"+json + "}";
        Field field;
        try {
            field = owner.getClass().getDeclaredField(columnName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(String.format("反序列化时发现实体类[%s]中不存在字段名为[%s]的字段",owner.getClass().getSimpleName(),columnName));
        }
        Class<?> type = field.getType();
        if (Map.class.isAssignableFrom(type)) {
            ResolvableType resolvableType = ResolvableType.forType(field.getGenericType());
            if (resolvableType.hasGenerics()) {
                Class<?> keyType = resolvableType.getGeneric(0).resolve();
                Class<?> valueType = resolvableType.getGeneric(1).resolve();
                MapType mapType = JsonUtil.getTypeFactory().constructMapType(HashMap.class, keyType, valueType);
                return JsonUtil.string2Object(json,mapType);
            }
        } else if (Collection.class.isAssignableFrom(type)) {
            ResolvableType resolvableType = ResolvableType.forType(field.getGenericType());
            if (resolvableType.hasGenerics()) {
                Class<?> elementClazz = resolvableType.getGeneric(0).resolve();
                CollectionType collectionType = JsonUtil.getTypeFactory().constructCollectionType(List.class, elementClazz);
                return JsonUtil.string2Object(json, collectionType);
            }
        }
        return JsonUtil.string2Object(json,type);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value == null){
            st.setString(index,"");
        } else {
            st.setString(index, JsonUtil.object2String(value));
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        if (value == null) {
            return null;
        }
        return JsonUtil.string2Object(JsonUtil.object2String(value),value.getClass());
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable)deepCopy(value);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy(original);
    }
}
