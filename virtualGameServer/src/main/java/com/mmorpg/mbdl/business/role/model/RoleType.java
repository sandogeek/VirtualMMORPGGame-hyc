package com.mmorpg.mbdl.business.role.model;

import com.baidu.bjf.remoting.protobuf.EnumReadable;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mmorpg.mbdl.common.jsonserializer.EnumReadableDeserializer;
import com.mmorpg.mbdl.common.jsonserializer.EnumReadableSerializer;

import java.util.Arrays;

/**
 * 角色类型
 *
 * @author Sando Geek
 * @since v1.0 2018/12/13
 **/
@JsonSerialize(using = EnumReadableSerializer.class)
@JsonDeserialize(using = EnumReadableDeserializer.class)
public enum RoleType implements EnumReadable {
    /**
     * 精灵
     */
    ELF(1,"精灵"),
    /**
     * 魔鬼
     */
    DEVIL(2,"魔鬼"),
    /**
     * 圣使
     */
    SAINT(3,"圣使");

    private int code;
    private String desc;
    RoleType(int code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return (byte)code;
    }


    public static RoleType getRoleTypeByCode(byte code){
        return Arrays.stream(values()).filter(roleType -> roleType.code == code).findAny().get();
    }

    @Override
    public int value() {
        return code;
    }
}
