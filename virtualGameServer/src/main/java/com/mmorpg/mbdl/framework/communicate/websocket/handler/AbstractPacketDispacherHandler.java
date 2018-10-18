package com.mmorpg.mbdl.framework.communicate.websocket.handler;

import com.google.common.base.Predicate;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketHandler;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import com.mmorpg.mbdl.framework.communicate.websocket.model.WSession;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.reflections.ReflectionUtils.getAllMethods;

/**
 * AbstractPacket包分发处理器，用于将AbstractPacket对象赋值给相应模块对应的处理方法的形参
 * (因为是Inbound所以只有请求包能到达这里)
 * 所以需要一个 Table <abstractPacket.getClass,method, bean（Object>
 * Reflections库以及spring的ReflectionUtils可以帮助完成以上需求
 * @author sando
 */
@ChannelHandler.Sharable
@Component
public class AbstractPacketDispacherHandler extends SimpleChannelInboundHandler<AbstractPacket>
                                            implements BeanPostProcessor {
    private static final Logger logger= LoggerFactory.getLogger(AbstractPacketDispacherHandler.class);
    // private Table<Class<?>, Object, Method> abstractPacket2Method2Object= HashBasedTable.create();
    private Map<Class<?>,Object> class2Object = new HashMap<>();
    private Map<Class<?>,Method> class2Method = new HashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractPacket abstractPacket) throws Exception {
        ReflectionUtils.invokeMethod(class2Method.get(abstractPacket.getClass()),class2Object.get(abstractPacket.getClass())
                ,new WSession(),abstractPacket);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        if (clazz.isAnnotationPresent(PacketHandler.class)){
            // 获取clazz中其中一个参数的父类或本身是AbstractPacket的所有方法
            Set<Method> methods=getAllMethods(clazz, withAnyParametersAssignableFrom(AbstractPacket.class));
            if (methods.size()==0){
                String message = String.format("类[%s]带@PacketHandler但却没有任何带AbstractPacket参数的方法",clazz.toString());
                logger.error(message);
                throw new  RuntimeException(message);
            }
            // 这些方法的参数数量应该为二，第一个参数应为WSession,
            // 第二个为AbstractPacket的子类（确认第一个参数后第二个参数为AbstractPacket的子类必然成立）
            // TODO 返回值校验并把返回值作为响应包发回去
            for (Method method : methods) {
                if (method.getParameterTypes().length!=2){
                    String message =String.format("方法[%s]必须为两个参数",
                            method.getDeclaringClass().getSimpleName()+"::"+method.getName());
                    throw new IllegalArgumentException(message);
                }
                if (method.getParameterTypes()[0] != WSession.class){
                    String message =String.format("方法[%s]第一个参数的类型必须为WSession",
                            method.getDeclaringClass().getSimpleName()+"::"+method.getName());
                }
                Class<?> clazz2 = method.getParameterTypes()[1];
                if (class2Method.keySet().contains(clazz2)) {
                    Method methodOld = class2Method.get(clazz2);
                    String message =
                            String.format("类型为[%s]的请求包同时被方法[%s]和方法[%s]处理", clazz2.getSimpleName(),
                                    methodOld.getDeclaringClass().getSimpleName()+"::"+methodOld.getName(),
                                    method.getDeclaringClass().getSimpleName()+"::"+method.getName());
                    throw new IllegalArgumentException(message);
                }
                class2Object.put(clazz2,bean);
                class2Method.put(clazz2,method);
            }
        }
        return bean;
    }

    /**
     * 看看方法任一参数的类型是不是任一给定的类型<types>的子类或本身，是则返回true
     * TODO 加到某个util中去
     * @param types
     * @return
     */
    public static Predicate<Method> withAnyParametersAssignableFrom(final Class... types) {
        return (@javax.annotation.Nullable Method input) -> {
                if (input != null) {
                    Class<?>[] parameterTypes = input.getParameterTypes();
                    for (Class<?> parameterType : parameterTypes) {
                        for (Class<?> type : types) {
                            if (type.isAssignableFrom(parameterType)){
                                return true;
                            }
                        }
                    }

                }
                return false;
            };
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
