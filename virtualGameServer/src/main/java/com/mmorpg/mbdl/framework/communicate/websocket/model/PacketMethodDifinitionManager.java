package com.mmorpg.mbdl.framework.communicate.websocket.model;

import com.google.common.base.Predicate;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketHandler;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.reflections.ReflectionUtils.getAllMethods;

@Component
public class PacketMethodDifinitionManager implements BeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(PacketMethodDifinitionManager.class);

    private static PacketMethodDifinitionManager self;
    public static PacketMethodDifinitionManager getIntance(){
        return self;
    }
    @PostConstruct
    private void init(){
        self = this;
    }
    private Map<Class<?>, PacketMethodDifinition> class2PacketMethodDifinition = new HashMap<>();
    private Map<Class<?>, Method> class2Method = new HashMap<>();

    public PacketMethodDifinition getPacketMethodDifinition(AbstractPacket abstractPacket){
        return class2PacketMethodDifinition.get(abstractPacket.getClass());
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, java.lang.String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        // TODO 如果PacketHandler类的请求处理方法很长，需要切分时可能会产生问题，到时候增加个可选注解即可，切分时用注解标明哪个是入口
        if (clazz.isAnnotationPresent(PacketHandler.class)){
            // 获取clazz中其中一个参数的父类或本身是AbstractPacket的所有方法
            Set<Method> methods=getAllMethods(clazz, withAnyParametersAssignableFrom(AbstractPacket.class));
            if (methods.size()==0){
                java.lang.String message = java.lang.String.format("类[%s]带@PacketHandler但却没有任何带AbstractPacket参数的方法",clazz.toString());
                logger.error(message);
                throw new  RuntimeException(message);
            }
            // 这些方法的参数数量应该为二，第一个参数应为WSession,
            // 第二个为AbstractPacket的子类（确认第一个参数后第二个参数为AbstractPacket的子类必然成立）
            // 返回值校验并把返回值作为响应包发回去
            for (Method method : methods) {
                if (method.getParameterTypes().length!=2){
                    java.lang.String message = java.lang.String.format("方法[%s]必须为两个参数",
                            method.getDeclaringClass().getSimpleName()+"::"+method.getName());
                    throw new IllegalArgumentException(message);
                }
                if ( !ISession.class.isAssignableFrom(method.getParameterTypes()[0])){
                    java.lang.String message = java.lang.String.format("方法[%s]第一个参数的类型必须为ISession或其子类",
                            method.getDeclaringClass().getSimpleName()+"::"+method.getName());
                    throw new IllegalArgumentException(message);
                }
                Class<?> abstractPacketClazz = method.getParameterTypes()[1];
                if (class2Method.keySet().contains(abstractPacketClazz)) {
                    Method methodOld = class2Method.get(abstractPacketClazz);
                    java.lang.String message =
                            java.lang.String.format("类型为[%s]的请求包同时被方法[%s]和方法[%s]处理", abstractPacketClazz.getSimpleName(),
                                    methodOld.getDeclaringClass().getSimpleName()+"::"+methodOld.getName(),
                                    method.getDeclaringClass().getSimpleName()+"::"+method.getName());
                    throw new IllegalArgumentException(message);
                }
                Class<?> returnType=method.getReturnType();
                if ( returnType!= void.class && !AbstractPacket.class.isAssignableFrom(returnType)){
                    java.lang.String message = java.lang.String.format("方法[%s]返回值只能是void或者AbstractPacket或其子类",
                            method.getDeclaringClass().getSimpleName()+"::"+method.getName());
                    throw new IllegalArgumentException(message);
                }
                class2Method.put(abstractPacketClazz,method);
                class2PacketMethodDifinition.put(abstractPacketClazz,
                        PacketMethodDifinition.valueOf(bean,method,abstractPacketClazz,method.getAnnotation(PacketMethod.class)));
                // logger.info("{}",class2PacketMethodDifinition.get(abstractPacketClazz).getPacketMethodAnno());
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
    public Object postProcessBeforeInitialization(Object bean, java.lang.String beanName) throws BeansException {
        return bean;
    }
}
