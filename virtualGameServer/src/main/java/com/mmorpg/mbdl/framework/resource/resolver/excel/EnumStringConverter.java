package com.mmorpg.mbdl.framework.resource.resolver.excel;

import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 枚举字符串转换器
 *
 * @author Sando Geek
 * @since v1.0 2018/12/3
 **/
public class EnumStringConverter extends AbstractConverter {
    @Override
    public Predicate<Field> getPredicate() {
        return field -> {
            if (field.getType().isEnum()||field.getType() == String.class){
                return true;
            }
            return false;
        };
    }

    @Override
    public Function<String, String> getFunction() {
        return s -> "\""+s+"\"";
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
