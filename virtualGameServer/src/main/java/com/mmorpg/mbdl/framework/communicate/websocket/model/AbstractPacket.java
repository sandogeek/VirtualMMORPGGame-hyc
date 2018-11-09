package com.mmorpg.mbdl.framework.communicate.websocket.model;

import com.mmorpg.mbdl.bussiness.common.PacketIdManager;
import com.mmorpg.mbdl.framework.common.generator.ProtoGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * 尽管其所有子类都有一个单例对象，但不要使用时注意不要获取容器中的单例，而应该使用new拿到新实例
 * 从容器中获取的话会导致，如果自己忘记覆盖某个字段，那么取那个字段时就取到了别人赋予的值
 * 弊端：容器中的单例对象容器启动完成后便失去了作用，但却一直占用内存（已在Spring IOC启动后去除容器中的这些单例）
 * TODO 与注解方式对比：启动时就把class加载到了内存并生成实例，加大了启动时间和内存占用，可以考虑改成注解方式，直接分析.class文件，不生成实例
 * @author sando
 */
public abstract class AbstractPacket {
    private static final Logger logger= LoggerFactory.getLogger(AbstractPacket.class);
    @Autowired
    private PacketIdManager packetIdManager;
    @Autowired
    private  ProtoGenerator protoGenerator;
    /**
     * 获取包id
     * @return PacketId
     */
    public abstract short getPacketId();

    @PostConstruct
    private void init(){
        protoGenerator.generateProto(this);
        packetIdManager.registerAbstractPacket(this);
        packetIdManager.registerCodec(this);
    }
}
