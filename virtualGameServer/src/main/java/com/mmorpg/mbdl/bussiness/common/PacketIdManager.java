package com.mmorpg.mbdl.bussiness.common;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger= LoggerFactory.getLogger(PacketIdManager.class);
    private BiMap<Short,Class<? extends AbstractPacket>> packetId2AbstractPacket = HashBiMap.create();
    // <packetId->protobuf编解码代理对象>
    private Map<Short, Codec> packetId2Codec = new HashMap<>();

    public static final short PING_HEART_BEAT = 1001;
    public static final short PONG_HEART_BEAT = 1002;

    public static final short LOGIN_AUTH_REQ = 10001;
    public static final short LOGIN_RESULT_RESP = 10002;

    public static final short CHAT_REQ = 10101;
    public static final short CHAT_RESP = 10102;


    private static PacketIdManager self;
    public static PacketIdManager getIntance(){
        return self;
    }
    @PostConstruct
    public void init(){
        self=this;
        // 把浏览器端的src\app\shared\model\packet和src\assets\proto作为一个仓库，每当新增协议后端push变更的内容
        // 前端利用tasks npm boot更新这两个文件夹的内容
        // 因为现在代码前后端在同一台机器，所以暂时直接生成到前端项目文件夹
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

    public BiMap<Short, Class<? extends AbstractPacket>> getPacketId2AbstractPacket() {
        return packetId2AbstractPacket;
    }
    public short getPacketId(Class<?> clazz){
        return packetId2AbstractPacket.inverse().get(clazz);
    }

    public Set<Class<? extends AbstractPacket>> getAbstractPackets(){
        return packetId2AbstractPacket.values();
    }
}