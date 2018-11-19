package com.mmorpg.mbdl.framework.storage.config.JetCache;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheConsts;
import com.alicp.jetcache.anno.support.CachedAnnoConfig;
import com.alicp.jetcache.anno.support.GlobalCacheConfig;
import com.google.common.base.Preconditions;
import com.mmorpg.mbdl.framework.common.utils.ReflectUtils;
import com.mmorpg.mbdl.framework.storage.annotation.JetCacheConfig;
import com.mmorpg.mbdl.framework.storage.core.IStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Field;

/**
 * JetCache的IStorage代理对象bean的后处理器
 *
 * @author Sando Geek
 * @since v1.0
 **/
public class JetCacheBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(JetCacheBeanPostProcessor.class);
    private ApplicationContext applicationContext;
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 参照{@link com.alicp.jetcache.anno.field.LazyInitCache#init()}完成多级缓存的创建，并注入到被代理对象
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof IStorage){
            try {
                Class<? extends IStorage> storageClazz = ((IStorage)bean).getClass();
                // 获取此代理对象的目标对象
                StorageJetCache storageJetCache = (StorageJetCache) ReflectUtils.getTarget(bean);
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
                    // storageJetCache.seteClazz(eClass);
                    Field cache = storageJetCache.getClass().getDeclaredField("cache");
                    cache.setAccessible(true);
                    cache.set(storageJetCache,getCache(eClass));
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return bean;
    }
    private Cache getCache(Class eClass){
        GlobalCacheConfig globalCacheConfig = applicationContext.getBean(GlobalCacheConfig.class);
        JetCacheConfig ann = (JetCacheConfig) eClass.getAnnotation(JetCacheConfig.class);
        Preconditions.checkNotNull(ann,"%s没有配置%s注解",eClass.getSimpleName(),JetCacheConfig.class.getSimpleName());
        // CreateCache ann = jetCacheConfig.createCache();
        CachedAnnoConfig cac = new CachedAnnoConfig();
        cac.setArea(ann.area());
        cac.setName(ann.name());
        cac.setTimeUnit(ann.timeUnit());
        cac.setExpire(ann.expire());
        cac.setLocalExpire(ann.localExpire());
        cac.setCacheType(ann.cacheType());
        cac.setLocalLimit(ann.localLimit());
        cac.setSerialPolicy(ann.serialPolicy());
        cac.setKeyConvertor(ann.keyCovertor());
        // cac.setRefreshPolicy(refreshPolicy);
        // cac.setPenetrationProtectConfig(protectConfig);

        String cacheName = cac.getName();
        if (CacheConsts.isUndefined(cacheName)) {
            cacheName = eClass.getSimpleName();
        }
        Cache cache = globalCacheConfig.getCacheContext().__createOrGetCache(cac, ann.area(), cacheName);
        return cache;
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
