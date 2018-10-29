package com.mmorpg.mbdl.framework.communicate.websocket.model;

import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketMethod;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * PacketMethod方法定义
 */
public class PacketMethodDifinition {
    private static final Logger logger = LoggerFactory.getLogger(PacketMethodDifinition.class);
    private Object bean;
    private Method method;
    private PacketMethod packetMethodAnno;
    // 如果没有@PacketMethod那么此方法上的注解作为默认注解
    @PacketMethod
    private void temp(){
    }
    public static PacketMethodDifinition valueOf(Object object,Method method,PacketMethod packetMethodAnno){
        PacketMethodDifinition packetMethodDifinition = new PacketMethodDifinition();
        packetMethodDifinition.setBean(object);
        packetMethodDifinition.setMethod(method);
        packetMethodDifinition.setPacketMethodAnno(packetMethodAnno);
        return packetMethodDifinition;
    }
    private Object invoke(WsSession wsSession,AbstractPacket abstractPacket){
        Object obj= org.springframework.util.ReflectionUtils.invokeMethod(method,bean,wsSession,abstractPacket);
        return obj;
    }
    public static void main(String[] args){
        Set<Method> methods = ReflectionUtils.getAllMethods(PacketMethodDifinition.class,ReflectionUtils.withAnnotation(PacketMethod.class));
        logger.info("{}",(Method) methods.toArray()[0]);
        // valueOf(new Object(),(Method) methods.toArray()[0]);
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

    public void setPacketMethodAnno(PacketMethod packetMethodAnno) {
        if (packetMethodAnno == null){
            Set<Method> methods = ReflectionUtils.getAllMethods(PacketMethodDifinition.class,ReflectionUtils.withAnnotation(PacketMethod.class));
            PacketMethod packetMethodAnn = ((Method)methods.toArray()[0]).getAnnotation(PacketMethod.class);
            this.packetMethodAnno = packetMethodAnn;
            return;
        }
        this.packetMethodAnno = packetMethodAnno;
    }
}
