package com.mmorpg.mbdl.framework.communicate.websocket.model;


import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketMethod;
import com.mmorpg.mbdl.framework.reflectasm.withunsafe.MethodAccess;
import com.mmorpg.mbdl.framework.thread.interfaces.Dispatchable;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * PacketMethod方法定义
 * @author Sando
 */
public class PacketMethodDefinition {
    private static final Logger logger = LoggerFactory.getLogger(PacketMethodDefinition.class);
    private static PacketMethod packetMethodAnnoStatic;
    static {
        Set<Method> methods = ReflectionUtils.getAllMethods(PacketMethodDefinition.class,ReflectionUtils.withAnnotation(PacketMethod.class));
        packetMethodAnnoStatic =((Method)methods.toArray()[0]).getAnnotation(PacketMethod.class);
    }
    private Object bean;
    private Method method;
    private MethodAccess methodAccess;
    private int methodIndex;
    // 日志打印用，AbstractPacket的类对象
    private Class<?> abstractPacketClazz;
    private PacketMethod packetMethodAnno;
    // 如果没有@PacketMethod那么此方法上的注解作为默认注解
    @PacketMethod
    private void temp(){
    }
    public static PacketMethodDefinition valueOf(Object object, Method method, Class<?> aClazz, PacketMethod packetMethodAnno){
        PacketMethodDefinition packetMethodDefinition = new PacketMethodDefinition();
        packetMethodDefinition.setBean(object);
        packetMethodDefinition.setMethodAccess(MethodAccess.access(object.getClass()));
        packetMethodDefinition.setMethodIndex(packetMethodDefinition.getMethodAccess().getIndex(method.getName()));
        packetMethodDefinition.setMethod(method);
        packetMethodDefinition.setPacketMethodAnno(packetMethodAnno);
        packetMethodDefinition.setAbstractPacketClazz(aClazz);
        return packetMethodDefinition;
    }
    public Object invoke(Dispatchable<? extends Serializable> dispatchable, AbstractPacket abstractPacket){
        /** 高性能反射调用
         *  {@link com.mmorpg.mbdl.ReflectASM.BenchMarkTest#main()}
         */
        return methodAccess.invoke(bean,methodIndex, dispatchable, abstractPacket);
    }
    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public PacketMethod getPacketMethodAnno() {
        return packetMethodAnno;
    }

    public MethodAccess getMethodAccess() {
        return methodAccess;
    }

    public void setMethodAccess(MethodAccess methodAccess) {
        this.methodAccess = methodAccess;
    }

    public int getMethodIndex() {
        return methodIndex;
    }

    public void setMethodIndex(int methodIndex) {
        this.methodIndex = methodIndex;
    }

    public void setPacketMethodAnno(PacketMethod packetMethodAnno) {
        if (packetMethodAnno == null){
            this.packetMethodAnno = packetMethodAnnoStatic;
            return;
        }
        this.packetMethodAnno = packetMethodAnno;
    }

    public Class<?> getAbstractPacketClazz() {
        return abstractPacketClazz;
    }

    public void setAbstractPacketClazz(Class<?> abstractPacketClazz) {
        this.abstractPacketClazz = abstractPacketClazz;
    }
}
