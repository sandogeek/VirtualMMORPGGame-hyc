package com.mmorpg.mbdl.bussiness.world.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.bussiness.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色刚进入场景时的所有可见物信息
 *
 * @author Sando Geek
 * @since v1.0 2018/12/20
 **/
@Component
@ProtoDesc(description = "首次进入场景可见信息响应包")
public class FirstEnterSceneResp extends AbstractPacket {
    @Protobuf(description = "所有可见信息响应包的集合",required = true)
    private List<AbstractPacket> abstractPacketList = new ArrayList<>(16);
    @Protobuf(description = "角色自身的可见信息")
    private AbstractPacket roleUiInfo;

    public FirstEnterSceneResp setRoleUiInfo(AbstractPacket roleUiInfo) {
        this.roleUiInfo = roleUiInfo;
        return this;
    }

    public List<AbstractPacket> getAbstractPacketList() {
        return abstractPacketList;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.FIRST_ENTER_SCENE_RESP;
    }
}

