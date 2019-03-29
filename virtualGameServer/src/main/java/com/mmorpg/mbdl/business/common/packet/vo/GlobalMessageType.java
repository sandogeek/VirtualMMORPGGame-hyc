package com.mmorpg.mbdl.business.common.packet.vo;

import com.baidu.bjf.remoting.protobuf.EnumReadable;

/**
 * 全局消息类型
 *
 * @author Sando Geek
 * @since v1.0 2019/3/6
 **/
// @JsonSerialize(using = EnumReadableSerializer.class)
// @JsonDeserialize(using = EnumReadableDeserializer.class)
public enum GlobalMessageType implements EnumReadable {
    /**
     * 成功
     */
    SUCCESS(1),
    /**
     * 错误
     */
    ERROR(2),
    /**
     * 提示
     */
    INFO(3),
    /**
     * 警告
     */
    WARNING(4);

    private int code;

    GlobalMessageType(int code) {
        this.code = code;
    }

    @Override
    public int value() {
        return code;
    }
}
