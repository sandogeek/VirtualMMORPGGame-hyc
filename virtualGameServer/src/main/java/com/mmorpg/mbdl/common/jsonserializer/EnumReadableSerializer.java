package com.mmorpg.mbdl.common.jsonserializer;

import com.baidu.bjf.remoting.protobuf.EnumReadable;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * EnumReadable类型的枚举序列化器
 *
 * @author Sando Geek
 * @since v1.0 2019/2/22
 **/
public class EnumReadableSerializer extends JsonSerializer<EnumReadable> {
    private static Set<Class> checkedAndSuccessClz = new HashSet<>();
    @Override
    public void serialize(EnumReadable value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (!value.getClass().isEnum()) {
            throw new RuntimeException("此序列化器不能用在非枚举类上");
        }
        if (!checkedAndSuccessClz.contains(value.getClass())) {
            check(value);
        }
        gen.writeNumber(value.value());
    }

    private void check(EnumReadable value) {
        EnumReadable[] enumConstants = value.getClass().getEnumConstants();
        Map<Integer,EnumReadable> enumReadableMap = new HashMap<>(enumConstants.length);
        for (EnumReadable enumReadable : enumConstants) {
            EnumReadable old = enumReadableMap.put(enumReadable.value(), enumReadable);
            if (old!=null) {
                throw new RuntimeException(String.format("在枚举类[%s]中,枚举[%s]与枚举[%s]的.value()值相同",
                        value.getClass().getSimpleName(),
                        ((Enum) old).name(),
                        ((Enum) enumReadable).name()
                ));
            }
        }
        checkedAndSuccessClz.add(value.getClass());
    }
}
