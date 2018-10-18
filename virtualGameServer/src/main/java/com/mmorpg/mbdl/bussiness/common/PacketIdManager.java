package com.mmorpg.mbdl.bussiness.common;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * websocket frame的id
 * @author sando
 */
@Component
public class PacketIdManager {
    private BiMap<Short,Class<? extends AbstractPacket>> packetId2AbstractPacket = HashBiMap.create();
    // <packetId->protobuf编解码代理对象>
    private Map<Short, Codec> packetId2Codec = new HashMap<>();

    public static final short LOGIN_AUTH_REQ = 10001;
    public static final short CHATREQ = 10101;

    private static PacketIdManager self;
    public static PacketIdManager getIntance(){
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

    public Set<Class<? extends AbstractPacket>> getAbstractPackets(){
        return packetId2AbstractPacket.values();
    }
}