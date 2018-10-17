package com.mmorpg.mbdl.bussiness.common;

import javax.annotation.PostConstruct;

/**
 * 尽管其所有子类都有一个单例对象，但不要使用时注意不要获取容器中的单例，而应该使用new拿到新实例
 * 从容器中获取的话会导致，如果自己忘记覆盖某个字段，那么取那个字段时就取到了别人赋予的值
 * 弊端：容器中的单例对象容器启动完成后便失去了作用，但却一直占用内存
 * 在Spring IOC启动后去除容器中的这些单例
 * @author sando
 */
public abstract class AbstractPacket {

    public abstract short getPacketId();

    @PostConstruct
    private void init(){
        PacketId.getIntance().registerAbstractPacket(this);
        PacketId.getIntance().registerCodec(this);
    }
}
