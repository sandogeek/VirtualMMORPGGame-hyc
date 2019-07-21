package com.mmorpg.mbdl.business.common;

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
 * 包Id管理器
 * TODO 通过注解定义Req、Resp后，这个类使用枚举的方式配置
 * @author sando
 */
@Component
public class PacketIdManager {
    private static final Logger logger= LoggerFactory.getLogger(PacketIdManager.class);
    private BiMap<Short,Class<? extends AbstractPacket>> packetId2AbstractPacket = HashBiMap.create();
    /**
     * <packetId->protobuf编解码代理对象>
      */
    private Map<Short, Codec> packetId2Codec = new HashMap<>();

    public static final short PING_HEART_BEAT = 1001;
    public static final short PONG_HEART_BEAT = 1002;
    public static final short GLOBAL_MESSAGE = 1003;
    /**
     * 热更
     */
    public static final short HOT_RELOAD_REQ = 1004;
    /** login */
    public static final short LOGIN_AUTH_REQ = 10001;
    public static final short LOGIN_RESULT_RESP = 10002;
    /** chat */
    public static final short CHAT_REQ = 10101;
    public static final short CHAT_RESP = 10102;
    public static final short CHAT_MESSAGE = 10103;
    /** register */
    public static final short REGISTER_REQ = 10201;
    public static final short REGISTER_RESP = 10202;
    /** role */
    public static final short GET_ROLE_LIST_REQ = 10301;
    public static final short GET_ROLE_LIST_RESP = 10302;
    public static final short ADD_ROLE_REQ = 10303;
    public static final short ADD_ROLE_RESP = 10304;
    public static final short DELETE_ROLE_REQ = 10305;
    public static final short DELETE_ROLE_RESP = 10306;
    public static final short CHOOSE_ROLE_REQ = 10307;
    public static final short CHOOSE_ROLE_RESP = 10308;
    public static final short CURRENT_HP_UPDATE = 10309;
    public static final short CURRENT_MP_UPDATE = 10310;
    public static final short MAX_HP_UPDATE = 10311;
    public static final short MAX_MP_UPDATE = 10312;
    public static final short LEVEL_UPDATE = 10313;
    public static final short EXP_UPDATE = 10314;
    /** world */
    public static final short SWITCH_SCENE_REQ = 10401;
    public static final short OBJECT_DISAPPEAR_RESP = 10402;
    public static final short ENTER_WORLD_REQ = 10403;
    public static final short SCENE_UI_INFO_RESP = 10404;
    /** object */
    public static final short ROLE_UI_INFO_RESP = 10501;
    public static final short CUSTOM_ROLE_UI_INFO_RESP = 10502;
    public static final short MONSTER_UI_INFO_RESP = 10503;
    public static final short MONSTER_HP_UPDATE = 10504;
    public static final short MONSTER_MAX_HP_UPDATE = 10505;
    /** container */
    public static final short GET_PACK_CONTENT_REQ = 10601;
    public static final short GET_PACK_CONTENT_RESP = 10602;
    public static final short USE_ITEM_REQ = 10603;
    public static final short USE_ITEM_RESP = 10604;
    public static final short ITEM_ADD_RESP = 10605;
    /**
     * skill
     */
    public static final short USE_SKILL_REQ = 10701;
    public static final short SKILL_LIST_UPDATE = 10702;

    private static PacketIdManager self;
    public static PacketIdManager getInstance(){
        return self;
    }
    @PostConstruct
    private void init(){
        self=this;
        // 把浏览器端的src\app\shared\model\packet和src\assets\proto作为一个仓库，每当新增协议后端push变更的内容
        // 前端利用tasks npm boot更新这两个文件夹的内容
        // 因为现在代码前后端在同一台机器，所以暂时直接生成到前端项目文件夹
    }

    public void registerAbstractPacket(AbstractPacket abstractPacket){
        if (packetId2AbstractPacket.keySet().contains(abstractPacket.getPacketId())){
            throw new RuntimeException(String.format("包[%s]与包[%s]id重复",
                    abstractPacket.getClass().getSimpleName(),packetId2AbstractPacket.get(abstractPacket.getPacketId()).getSimpleName()));
        }
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