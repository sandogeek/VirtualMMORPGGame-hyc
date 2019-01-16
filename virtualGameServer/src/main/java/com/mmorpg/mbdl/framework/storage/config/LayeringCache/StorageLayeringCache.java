package com.mmorpg.mbdl.framework.storage.config.LayeringCache;

import com.github.xiaolyuh.annotation.FirstCache;
import com.github.xiaolyuh.annotation.SecondaryCache;
import com.github.xiaolyuh.cache.Cache;
import com.github.xiaolyuh.manager.CacheManager;
import com.github.xiaolyuh.setting.FirstCacheSetting;
import com.github.xiaolyuh.setting.LayeringCacheSetting;
import com.github.xiaolyuh.setting.SecondaryCacheSetting;
import com.google.common.base.Preconditions;
import com.mmorpg.mbdl.framework.storage.annotation.LayeringCacheConfig;
import com.mmorpg.mbdl.framework.storage.core.AbstractEntity;
import com.mmorpg.mbdl.framework.storage.core.EntityCreator;
import com.mmorpg.mbdl.framework.storage.core.IStorage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * IStorage的默认实现类
 * @param <PK> 主键类型
 * @param <E> 实体类型
 * @author sando
 */
@NoRepositoryBean
public class StorageLayeringCache<PK extends Serializable &Comparable<PK>,E extends AbstractEntity<PK>> extends SimpleJpaRepository<E,PK>
        implements IStorage<PK,E> {
    private static final Logger logger = LoggerFactory.getLogger(StorageLayeringCache.class);

    /** IStorageBeanPostProcessor中注入 */
    private CacheManager cacheManager;
    /** 泛型E的实际类型 */
    private Class<? extends AbstractEntity> eClazz;

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void seteClazz(Class<? extends AbstractEntity> eClazz) {
        this.eClazz = eClazz;
    }

    public StorageLayeringCache(JpaEntityInformation<E, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    /**
     * 根据entityClass获取缓存
     * @return Cache
     */
    private Cache getCache(){
        LayeringCacheConfig layeringCacheConfig = eClazz.getAnnotation(LayeringCacheConfig.class);
        Preconditions.checkNotNull(layeringCacheConfig,"实体类[%s]没有使用@CacheConfig配置缓存",eClazz.getSimpleName());
        String cacheName = layeringCacheConfig.cacheName();
        if (StringUtils.isEmpty(cacheName)){
            cacheName = eClazz.getSimpleName();
        }
        FirstCache firstCache = layeringCacheConfig.firstCache();
        SecondaryCache secondaryCache = layeringCacheConfig.secondaryCache();
        FirstCacheSetting firstCacheSetting = new FirstCacheSetting(firstCache.initialCapacity(), firstCache.maximumSize(),
                firstCache.expireTime(), firstCache.timeUnit(), firstCache.expireMode());

        SecondaryCacheSetting secondaryCacheSetting = new SecondaryCacheSetting(secondaryCache.expireTime(),
                secondaryCache.preloadTime(), secondaryCache.timeUnit(), secondaryCache.forceRefresh());
        LayeringCacheSetting layeringCacheSetting = new LayeringCacheSetting(firstCacheSetting, secondaryCacheSetting, layeringCacheConfig.depict());
        // 通过cacheName和缓存配置获取Cache
        Cache cache = cacheManager.getCache(cacheName, layeringCacheSetting);
        return cache;
    }

    /**
     * 因为调用自身的saveAndFlush时，aop无法完成切面增强，所以这里必须加上@Transactional
     * @param entity 待插入的实体
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public E create(E entity) {
        Cache cache = getCache();
        E entityAfterSave = this.saveAndFlush(entity);
        cache.put(entity.getId(),entity);
        return entityAfterSave;
    }

    public E getFromCache(PK id,Class<? extends AbstractEntity> eClazz){
        Cache cache = getCache();
        return (E)cache.get(id,eClazz);
    }

    @Override
    public E get(PK id) {
        return null;
    }

    @Override
    public E getOrCreate(PK id, EntityCreator<PK, E> entityCreator) {
        return null;
    }

    @Override
    public E update(E entity) {
        return null;
    }

    @Override
    public E remove(PK id) {
        return null;
    }

    @Override
    public void mergeUpdate(E entity) {

    }
}
