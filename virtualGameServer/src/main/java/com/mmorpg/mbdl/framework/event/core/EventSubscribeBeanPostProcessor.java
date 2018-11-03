package com.mmorpg.mbdl.framework.event.core;

import com.google.common.eventbus.Subscribe;
import org.reflections.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class EventSubscribeBeanPostProcessor implements BeanPostProcessor {

    /** 事件总线bean由Spring IoC容器负责创建，这里只需要通过@Autowired注解注入该bean即可使用事件总线 */
    @Autowired
    SyncEventBus syncEventBus;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    /** 对于每个容器执行了初始化的 bean，如果这个 bean 的某个方法注解了@Subscribe,则将该 bean 注册到事件总线 */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        int size = ReflectionUtils.getAllMethods(bean.getClass(),ReflectionUtils.withAnnotation(Subscribe.class)).size();
        if (size>0){
            syncEventBus.register(bean);
        }
        return bean;
    }
}

