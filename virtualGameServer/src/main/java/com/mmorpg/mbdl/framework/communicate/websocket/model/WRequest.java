package com.mmorpg.mbdl.framework.communicate.websocket.model;

/**
 * websocket请求包
 * @author sando
 */
public class WRequest {
    // 请求包id
    private short packetId;
    // protobuf编码的字节数据
    private byte[] data;

    public static WRequest valueOf(short packetId,byte[] data){
        WRequest wRequest = new WRequest();
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
