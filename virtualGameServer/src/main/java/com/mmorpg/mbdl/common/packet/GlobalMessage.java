package com.mmorpg.mbdl.common.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.common.PacketIdManager;
import com.mmorpg.mbdl.common.packet.vo.GlobalMessageType;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

/**
 * 全局信息反馈
 *
 * @author Sando Geek
 * @since v1.0 2019/2/26
 **/
@ProtoDesc(description = "全局信息反馈")
public class GlobalMessage extends AbstractPacket {
    @Protobuf(description = "消息类型", required = true)
    private GlobalMessageType globalMessageType = GlobalMessageType.INFO;
    @Protobuf(description = "反馈的消息")
    private String message;

    public GlobalMessage() {
    }

    public GlobalMessage(String message) {
        this.message = message;
    }

    public GlobalMessage(GlobalMessageType globalMessageType, String message) {
        this.globalMessageType = globalMessageType;
        this.message = message;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.GLOBAL_MESSAGE;
    }
}