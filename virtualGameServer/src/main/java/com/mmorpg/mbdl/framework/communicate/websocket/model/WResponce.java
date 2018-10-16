package com.mmorpg.mbdl.framework.communicate.websocket.model;

/**
 * websocket响应包
 * @author sando
 */
public class WResponce {
    // 请求包id
    private short packetId;
    // protobuf编码的字节数据
    private byte[] data;

    public static WResponce valueOf(short packetId,byte[] data){
        WResponce wRequest = new WResponce();
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
