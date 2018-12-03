package com.mmorpg.mbdl.framework.resource.resolver.excel;

import org.springframework.util.ObjectUtils;

/**
 * 抽象转换器
 *
 * @author Sando Geek
 * @since v1.0 2018/12/3
 **/
public abstract class AbstractConverter implements IConverter {
    private final int hashCode = getOrder();
    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof IConverter)) {
            return false;
        }
        IConverter otherType = (IConverter) other;
        if (!ObjectUtils.nullSafeEquals(this.getOrder(), otherType.getOrder())) {
            return false;
        }
        return true;
    }
}
