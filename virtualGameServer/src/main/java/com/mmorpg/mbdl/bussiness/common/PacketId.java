package com.mmorpg.mbdl.bussiness.common;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * websocket frame的id
 * @author sando
 */
@Component
public class PacketId {
    private BiMap<Short,Class<? extends AbstractPacket>> packetId2AbstractPacket = HashBiMap.create();
    // <packetId->protobuf编解码代理对象>
    private Map<Short, Codec> packetId2Codec = new HashMap<>();

    public static final short LOGIN_AUTH_REQ = 10001;

    private static PacketId self;
    public static PacketId getIntance(){
        return self;
    }
    @PostConstruct
    public void init(){
        self=this;
    }

    public void registerAbstractPacket(AbstractPacket abstractPacket){
        packetId2AbstractPacket.put(abstractPacket.getPacketId(),abstractPacket.getClass());
    }
    public void registerCodec(AbstractPacket abstractPacket){
        packetId2Codec.put(abstractPacket.getPacketId(),ProtobufProxy.create(abstractPacket.getClass()));
    }

    public Codec getCodec(short packetId){
        return this.packetId2Codec.get(packetId);
    }
}