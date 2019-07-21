package com.mmorpg.mbdl.framework.resource.core;

import com.mmorpg.mbdl.framework.common.utils.ProtostuffUtils;

/**
 * protostuff编解码器
 *
 * @author Sando Geek
 * @since v1.0 2019/7/22
 **/
public class ProtostuffCodec<T> {
    private Class<T> cls;

    public ProtostuffCodec(Class<T> cls) {
        this.cls = cls;
    }

    public byte[] encode(T t) {
        return ProtostuffUtils.serialize(t);
    }

    public T decode(byte[] bytes) {
        return ProtostuffUtils.deserialize(bytes, cls);
    }
}
