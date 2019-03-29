package com.mmorpg.mbdl.common.orm;

import com.baidu.bjf.remoting.protobuf.EnumReadable;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * 枚举出入库转换
 *
 * @author Sando Geek
 * @since v1.0 2019/2/25
 **/
public class EnumReadableType implements UserType {
    public static final String NAME = "enum";

    @Override
    public int[] sqlTypes() {
        return new int[] {Types.INTEGER};
    }

    @Override
    public Class returnedClass() {
        return EnumReadable.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return ((EnumReadable) x).value() == ((EnumReadable) y).value();
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return ObjectUtils.nullSafeHashCode(x);
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        int value = rs.getInt(names[0]);
        if (owner == null) {
            return value;
        }
        String columnName = rs.getMetaData().getColumnName(rs.findColumn(names[0]));
        Field field = OrmUtil.getFieldByClassAndColumnName(owner.getClass(), columnName);
        Class<?> type = field.getType();
        if (!type.isEnum()) {
            throw new RuntimeException(String.format("类[%s]中的字段[%s]类型不是枚举",owner.getClass().getSimpleName(),field.getName()));
        }
        for (Enum e :
                (Enum[]) type.getEnumConstants()) {
            if (((EnumReadable) e).value() == value) {
                return e;
            }
        }
        throw new RuntimeException(String.format("枚举类型[%s]中不存在EnumReadable.value()==%s的枚举",type.getSimpleName(),value));
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value==null) {
            st.setNull(index,Types.INTEGER);
            return;
        }
        st.setInt(index, ((EnumReadable) value).value());
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        if (value == null) {
            return null;
        }
        return ClonerComponent.getInstance().deepClone(value);
    }

    @Override
    public boolean isMutable() {
        return false;
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
