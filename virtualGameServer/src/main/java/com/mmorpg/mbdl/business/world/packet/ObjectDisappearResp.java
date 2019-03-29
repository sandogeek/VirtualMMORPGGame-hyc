package com.mmorpg.mbdl.business.world.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.business.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;

/**
 * 可见物消失响应包
 *
 * @author Sando Geek
 * @since v1.0 2018/12/24
 **/
@Component
@ProtoDesc(description = "可见物消失响应包")
public class ObjectDisappearResp extends AbstractPacket {
    @Protobuf(description = "可见物id",required = true)
    private Long id;

    public ObjectDisappearResp setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.OBJECT_DISAPPEAR_RESP;
    }
}