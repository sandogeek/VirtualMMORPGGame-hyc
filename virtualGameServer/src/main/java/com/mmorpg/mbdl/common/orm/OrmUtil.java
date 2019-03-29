package com.mmorpg.mbdl.common.orm;

import com.google.common.base.Predicate;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;
import org.reflections.ReflectionUtils;

import javax.persistence.Column;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * orm工具类
 *
 * @author Sando Geek
 * @since v1.0 2019/2/25
 **/
public class OrmUtil {
    private static Table<Class,String, Field> class2ColumnName2FieldTable = HashBasedTable.create();

    /**
     * 给自定义的{@link org.hibernate.usertype.UserType}提供缓存功能
     * @param clz
     * @param columnName
     * @return
     */
    public static Field getFieldByClassAndColumnName(Class clz,String columnName) {
        if (!class2ColumnName2FieldTable.containsRow(clz)) {
            Set<Field> fieldsWithJsonType = ReflectionUtils.getFields(clz, withJsonTypeOrEnumReadableType());
            fieldsWithJsonType.forEach(fieldTemp -> {
                String columnNameResult;
                if (fieldTemp.isAnnotationPresent(Column.class)) {
                    Column column = fieldTemp.getAnnotation(Column.class);
                    columnNameResult = StringUtils.isEmpty(column.name())?fieldTemp.getName():column.name();
                } else {
                    columnNameResult = fieldTemp.getName();
                }
                class2ColumnName2FieldTable.put(clz, columnNameResult, fieldTemp);
            });
        }
        return class2ColumnName2FieldTable.get(clz, columnName);
    }

    private static  <T extends AnnotatedElement> Predicate<T> withJsonTypeOrEnumReadableType() {
        return input -> input != null && input.isAnnotationPresent(Type.class) &&
                isJsonTypeOrEnumReadableType(input.getAnnotation(Type.class));
    }

    private static boolean isJsonTypeOrEnumReadableType(Type type) {
        return JsonType.NAME.equals(type.type())||EnumReadableType.NAME.equals(type.type());
    }
}
