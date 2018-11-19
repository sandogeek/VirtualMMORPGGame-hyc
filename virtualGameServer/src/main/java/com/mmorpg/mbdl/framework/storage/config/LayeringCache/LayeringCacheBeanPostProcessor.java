package com.mmorpg.mbdl.framework.storage.config.LayeringCache;

import com.github.xiaolyuh.manager.CacheManager;
import com.mmorpg.mbdl.framework.common.utils.ReflectUtils;
import com.mmorpg.mbdl.framework.storage.core.IStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;

/**
 * IStorage后处理器
 * @author Sando Geek
 */
public class LayeringCacheBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(LayeringCacheBeanPostProcessor.class);
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
                StorageLayeringCache storageLayeringCache = (StorageLayeringCache) ReflectUtils.getTarget(bean);
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
                    storageLayeringCache.seteClazz(eClass);
                }
                CacheManager cacheManager = applicationContext.getBean(CacheManager.class);
                storageLayeringCache.setCacheManager(cacheManager);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return bean;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
