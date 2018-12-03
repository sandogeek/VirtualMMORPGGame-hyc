package com.mmorpg.mbdl.framework.resource.resolver.excel;

import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 转换器，用于针对某种类型自定义Excel单元格的字符串格式
 *
 * @author Sando Geek
 * @since v1.0
 */
public interface IConverter {
    /**
     * 判断该属性是否需要被此属性转换器处理，一旦满足这个Predicate，就会应用转换函数，优先级更低的属性转换器将不会被应用
     * @return 字段判断器
     */
    Predicate<Field> getPredicate();
    /**
     * 获取用于转换的转换函数，
     * @return 转换函数
     */
    Function<String,String> getFunction();

    /**
     * 属性转换器的优先级，越大优先级越高，自带的转换器优先级从-100开始，建议自定义的转换器从0开始，负数预留给框架
     * @return 优先级
     */
    int getOrder();
}
