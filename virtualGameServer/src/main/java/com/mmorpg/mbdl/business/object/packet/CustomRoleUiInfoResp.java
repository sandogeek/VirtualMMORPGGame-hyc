package com.mmorpg.mbdl.business.object.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.business.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import org.springframework.stereotype.Component;

/**
 * 自身信息，比看到的其它玩家的角色有更多的信息
 *
 * @author Sando Geek
 * @since v1.0 2018/12/26
 **/
@Component
@ProtoDesc(description = "自身信息")
public class CustomRoleUiInfoResp extends RoleUiInfoResp {
    @Protobuf(description = "当前血量",required = true)
    private long currentHp;
    @Protobuf(description = "当前蓝量",required = true)
    private long currentMp;
    @Protobuf(required = true,description = "最大血量")
    private long maxHp;
    @Protobuf(required = true,description = "最大蓝量")
    private long maxMp;

    public CustomRoleUiInfoResp setCurrentHp(long currentHp) {
        this.currentHp = currentHp;
        return this;
    }

    public CustomRoleUiInfoResp setCurrentMp(long currentMp) {
        this.currentMp = currentMp;
        return this;
    }

    public CustomRoleUiInfoResp setMaxHp(long maxHp) {
        this.maxHp = maxHp;
        return this;
    }

    public CustomRoleUiInfoResp setMaxMp(long maxMp) {
        this.maxMp = maxMp;
        return this;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.CUSTOM_ROLE_UI_INFO_RESP;
    }
}