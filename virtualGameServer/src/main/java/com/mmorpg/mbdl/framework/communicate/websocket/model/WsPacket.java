package com.mmorpg.mbdl.framework.communicate.websocket.model;

/**
 * websocket字节包
 * @author sando
 */
public class WsPacket {
    // 请求包id
    private short packetId;
    // protobuf编码的字节数据
    private byte[] data;

    public static WsPacket valueOf(short packetId, byte[] data){
        WsPacket wRequest = new WsPacket();
        wRequest.setPacketId(packetId);
        wRequest.setData(data);
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

    public void setData(byte[] data) {
        this.data = data;
    }
}
