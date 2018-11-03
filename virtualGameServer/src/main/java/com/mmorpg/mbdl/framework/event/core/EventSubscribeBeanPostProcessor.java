package com.mmorpg.mbdl.framework.event.core;

import com.google.common.eventbus.Subscribe;
import org.reflections.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class EventSubscribeBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

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
            applicationContext.getBean(SyncEventBus.class).register(bean);
        }
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

