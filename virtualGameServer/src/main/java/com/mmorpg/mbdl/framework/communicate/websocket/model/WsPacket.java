package com.mmorpg.mbdl.framework.communicate.websocket.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * websocket字节包
 * @author sando
 */
@SuppressFBWarnings("EI_EXPOSE_REP")
public class WsPacket {
    // 请求包id
    private short packetId;
    // protobuf编码的字节数据
    private byte[] data;

    public static WsPacket valueOf(short packetId, byte[] data){
        WsPacket wRequest = new WsPacket();
        wRequest.setPacketId(packetId);
        wRequest.data = data;
        return wRequest;
    }

    public short getPacketId() {
        return packetId;
    }

    public void setPacketId(short packetId) {
        this.packetId = packetId;
    }

    public byte[] getData() {
        return data;
    }

}
