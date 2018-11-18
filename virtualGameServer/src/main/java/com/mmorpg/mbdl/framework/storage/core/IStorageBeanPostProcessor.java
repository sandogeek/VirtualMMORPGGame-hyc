package com.mmorpg.mbdl.framework.storage.core;

import com.github.xiaolyuh.manager.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * IStorage后处理器
 * @author Sando Geek
 */
@Component
public class IStorageBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(IStorageBeanPostProcessor.class);
    private ApplicationContext applicationContext;
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof IStorage){
            try {
                Class<? extends IStorage> storageClazz = ((IStorage)bean).getClass();
                // 获取此代理对象的目标对象
                StorageMySql storageMySql = (StorageMySql)getTarget(bean);
                Class<IStorage> daoClass =null;
                for (Class clz:storageClazz.getInterfaces()){
                    if (IStorage.class.isAssignableFrom(clz)){
                        daoClass = clz;
                        break;
                    }
                }
                ResolvableType resolvableType = null;
                if (daoClass!=null){
                    resolvableType = ResolvableType.forType(daoClass.getGenericInterfaces()[0]);
                    // 获取dao类泛型接口的第二个具体泛型，E extends IEntity的具体的E
                    Class eClass = resolvableType.getGeneric(1).resolve();
                    storageMySql.seteClazz(eClass);
                }
                CacheManager cacheManager = applicationContext.getBean(CacheManager.class);
                storageMySql.setCacheManager(cacheManager);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return bean;
    }
    /**
     * 获取 目标对象
     * @param proxy 代理对象
     * @return
     * @throws Exception
     */
    public static Object getTarget(Object proxy) throws Exception {

        if(!AopUtils.isAopProxy(proxy)) {
            //不是代理对象
            return proxy;
        }

        if(AopUtils.isJdkDynamicProxy(proxy)) {
            return getJdkDynamicProxyTargetObject(proxy);
        } else {
            //cglib
            return getCglibProxyTargetObject(proxy);
        }



    }


    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);

        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        Object target = ((AdvisedSupport)advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();

        return target;
    }


    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);

        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        Object target = ((AdvisedSupport)advised.get(aopProxy)).getTargetSource().getTarget();

        return target;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
