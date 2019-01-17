package com.mmorpg.mbdl.business.common.orm;

import com.baidu.bjf.remoting.protobuf.EnumReadable;

import javax.persistence.AttributeConverter;

/**
 * 枚举属性转换器
 *
 * @author Sando Geek
 * @since v1.0 2019/1/17
 **/
public class EnumConverter implements AttributeConverter<EnumReadable, Integer> {
    // private Class<X> xclazz;
    // private Method valuesMethod;
    //
    // @SuppressWarnings("unchecked")
    // public EnumConverter() {
    //
    //     this.xclazz = (Class<X>) (((ParameterizedType) this.getClass().getGenericSuperclass())
    //             .getActualTypeArguments())[0];
    //     try {
    //         valuesMethod = xclazz.getMethod("values");
    //     } catch (Exception e) {
    //         throw new RuntimeException("can't get values method from " + xclazz);
    //     }
    // }

    @Override
    public Integer convertToDatabaseColumn(EnumReadable attribute) {
        if (!attribute.getClass().isEnum()) {
            throw new RuntimeException(String.format("此转换器只能在枚举类型上使用，实际类型[%s]",attribute.getClass().getSimpleName()));
        }
        return attribute.value();
    }

    @Override
    public EnumReadable convertToEntityAttribute(Integer dbData) {
        return null;
    }

    // public Y convertToDatabaseColumn(BaseEnum<Y> attribute) {
    //     return attribute == null ? null : attribute.getValue();
    // }
    //
    // @SuppressWarnings("unchecked")
    // public X convertToEntityAttribute(Y dbData) {
    //     try {
    //         X[] values = (X[]) valuesMethod.invoke(null);
    //         for (X x : values) {
    //             if (x.getValue().equals(dbData)) {
    //                 return x;
    //             }
    //         }
    //     } catch (Exception e) {
    //         throw new RuntimeException("can't convertToEntityAttribute" + e.getMessage());
    //     }
    //     throw new RuntimeException("unknown dbData " + dbData);
    // }
}
