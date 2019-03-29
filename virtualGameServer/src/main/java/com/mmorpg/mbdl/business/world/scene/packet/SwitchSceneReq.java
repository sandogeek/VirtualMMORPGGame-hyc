package com.mmorpg.mbdl.business.world.scene.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;

/**
 * 切换场景请求
 *
 * @author Sando Geek
 * @since v1.0 2018/12/27
 **/
@Component
@ProtoDesc(description = "切换场景请求")
public class SwitchSceneReq extends AbstractPacket {
    @Protobuf(description = "目标场景Id",required = true)
    private int targetSceneId;

    public int getTargetSceneId() {
        return targetSceneId;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.SWITCH_SCENE_REQ;
    }
}