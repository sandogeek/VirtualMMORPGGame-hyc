package com.mmorpg.mbdl.framework.storage.config.JetCache;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheGetResult;
import com.google.common.base.Preconditions;
import com.mmorpg.mbdl.framework.common.utils.JsonUtil;
import com.mmorpg.mbdl.framework.storage.annotation.JetCacheConfig;
import com.mmorpg.mbdl.framework.storage.core.AbstractEntity;
import com.mmorpg.mbdl.framework.storage.core.EntityCreator;
import com.mmorpg.mbdl.framework.storage.core.IStorage;
import com.mmorpg.mbdl.framework.thread.task.DelayedTask;
import com.mmorpg.mbdl.framework.thread.task.TaskDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 使用JetCache的IStorage默认实现类
 *
 * @author Sando Geek
 * @since v1.0
 **/
@NoRepositoryBean
public class StorageJetCache <PK extends Serializable &Comparable<PK>,E extends AbstractEntity<PK>> extends SimpleJpaRepository<E,PK>
        implements IStorage<PK,E> {
    private static final Logger logger = LoggerFactory.getLogger(StorageJetCache.class);
    private Cache<PK,E> cache;
    /** {@link JetCacheConfig#delay()} */
    private int delay;
    /** 代理对象 */
    private IStorage<PK,E> proxy;

    private final EntityManager entityManager;


    public StorageJetCache(JpaEntityInformation<E, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public E create(E entity) {
        Preconditions.checkNotNull(entity,"entity不能为null");
        CacheGetResult<E> cacheGetResult = cache.GET(entity.getId());
        if (cacheGetResult.isSuccess()){
            E entityFromCache = cacheGetResult.getValue();
            // 缓存中缓存了其null值，说明数据库中没有，直接存库
            if (entityFromCache == null) {
                return doInsert(entity);
            }
            // 缓存中有，说明数据库中也有，那么不应该创建，抛出异常
            else {
                throw new EntityExistsException("数据库中已存在该实体，考虑使用update?");
            }
        }
        // 缓存中没有成功获取就查数据库
        else {
            if (exists(entity.getId())){
                throw new EntityExistsException("数据库中已存在该实体，考虑使用update?");
            }else {
                return doInsert(entity);
            }
        }
    }

    private E doInsert(E entity) {
        E e = saveAndFlush(entity);
        cache.put(entity.getId(),e);
        return e;
    }

    private void doUpdate(E entity){
        // merge返回的实体和参数的实体不一致,原因在于merge方法会调用userType的deepCopy来构造复制的对象，而使用jackson序列化时JsonType中deepCopy无法获取泛型
        PK id = entity.getId();
        E merge = saveAndFlush(entity);
        cache.put(id, merge);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(E entity) {
        PK id = entity.getId();
        Preconditions.checkNotNull(id,"id不能为null");
        CacheGetResult<E> cacheGetResult = cache.GET(id);
        if (cacheGetResult.isSuccess()){
            E entityFromCache = cacheGetResult.getValue();
            // 缓存中缓存了其null值，说明数据库中没有
            if (entityFromCache == null) {
                throw new EntityNotFoundException("数据库中不存在该实体，先create一下？");
            }
            // 缓存中有，说明数据库中也有
            else {
                executeUpdate(entity);
            }
        }else {
            if (exists(entity.getId())){
                executeUpdate(entity);
            }else {
                throw new EntityNotFoundException("数据库中不存在该实体，先create一下？");
            }
        }
    }

    private void executeUpdate(E entity) {
        // TODO 执行update时取消先前的合并更新任务（如果有的话）
        doUpdate(entity);
    }

    @Override
    public void mergeUpdate(E entity) {
        if (this.delay == 0) {
            proxy.update(entity);
            return;
        }
        else if (!entity.getCanCreateMergeUpdateTask().compareAndSet(true, false)) {
            return;
        }
        TaskDispatcher.getInstance().dispatch(new DelayedTask(null, delay, TimeUnit.SECONDS) {
            @Override
            public String taskName() {
                return String.format("合并更新实体[%s]：%s", entity.getClass().getSimpleName(), JsonUtil.object2String(entity));
            }

            @Override
            public void execute() {
                try {
                    proxy.update(entity);
                } finally {
                    entity.getCanCreateMergeUpdateTask().set(true);
                }
            }
        }.setLogOrNot(true).setMaxExecuteTime(30, TimeUnit.MILLISECONDS), true);
    }

    @Override
    public E get(PK id) {
        Preconditions.checkNotNull(id,"id不能为null");
        CacheGetResult<E> cacheGetResult = cache.GET(id);
        if (cacheGetResult.isSuccess()){
            E entityFromCache = cacheGetResult.getValue();
            return entityFromCache;
        }else {
            E entity = findOne(id);
            // 不管是null还是实体都缓存起来
            cache.put(id,entity);
            return entity;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public E getOrCreate(PK id, EntityCreator<PK, E> entityCreator) {
        Preconditions.checkNotNull(id,"id不能为null");
        E e = get(id);
        if (e!=null){
            return e;
        }else {
            E entityCreated = entityCreator.create(id);
            return doInsert(entityCreated);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public E remove(PK id) {
        Preconditions.checkNotNull(id,"id不能为null");
        CacheGetResult<E> cacheGetResult = cache.GET(id);
        if (cacheGetResult.isSuccess()){
            E entity = cacheGetResult.getValue();
            // 缓存中缓存了其null值，说明数据库中没有，直接返回null
            if (entity == null) {
                return entity;
            }
            // 缓存中有，说明数据库中也有
            else {
                delete(entity);
                cache.put(id,null);
                return entity;
            }
        }else {
            // 缓存没有直接查库
            E entity = findOne(id);
            if (entity != null){
                delete(entity);
                cache.put(id,null);
            }
            return entity;
        }
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setProxy(IStorage<PK, E> proxy) {
        this.proxy = proxy;
    }

    public void setCache(Cache<PK, E> cache) {
        this.cache = cache;
    }
}
