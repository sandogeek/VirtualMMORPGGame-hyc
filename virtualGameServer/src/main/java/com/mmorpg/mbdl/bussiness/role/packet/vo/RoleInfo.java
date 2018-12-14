package com.mmorpg.mbdl.bussiness.role.packet.vo;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.bussiness.role.model.RoleType;

/**
 * 角色信息
 *
 * @author Sando Geek
 * @since v1.0 2018/12/14
 **/
public class RoleInfo {
    @Protobuf(required = true,description = "角色名称")
    private String name;
    @Protobuf(required = true,description = "角色等级")
    private short level;
    @Protobuf(required = true,description = "角色类型")
    private RoleType roleTypeCode;
}
