package com.mmorpg.mbdl.bussiness.role.model;

import java.util.Arrays;

/**
 * 角色类型
 *
 * @author Sando Geek
 * @since v1.0 2018/12/13
 **/
public enum RoleType {
    /**
     * 精灵
     */
    ELF((byte)1,"精灵"),
    /**
     * 魔鬼
     */
    DEVIL((byte)2,"魔鬼"),
    /**
     * 圣使
     */
    SAINT((byte)3,"圣使");

    private byte code;
    private String desc;
    RoleType(byte code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public static RoleType getRoleTypeByCode(byte code){
        return Arrays.stream(values()).filter(roleType -> roleType.code == code).findAny().get();
    }
}
