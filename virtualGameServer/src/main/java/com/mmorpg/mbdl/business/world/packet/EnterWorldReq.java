package com.mmorpg.mbdl.business.world.packet;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.mmorpg.mbdl.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;

/**
 * 进入世界请求
 *
 * @author Sando Geek
 * @since v1.0 2018/12/24
 **/
@Component
@ProtobufClass
@ProtoDesc(description = "进入世界请求")
public class EnterWorldReq extends AbstractPacket {

    @Override
    public short getPacketId() {
        return PacketIdManager.ENTER_WORLD_REQ;
    }
}