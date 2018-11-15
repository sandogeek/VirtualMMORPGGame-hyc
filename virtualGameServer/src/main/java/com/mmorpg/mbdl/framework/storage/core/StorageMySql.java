package com.mmorpg.mbdl.framework.storage.core;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * IStorage的默认实现类
 * @param <PK> 主键类型
 * @param <E> 实体类型
 * @author sando
 */
@NoRepositoryBean
public class StorageMySql<PK extends Serializable &Comparable<PK>,E extends IEntity<PK>> extends SimpleJpaRepository<E,PK> implements IStorage<PK,E> {

    // private final JpaEntityInformation<E, ?> entityInformation;
    // private final EntityManager em;
    // private final PersistenceProvider provider;
    // @PersistenceContext
    // private EntityManager entityManager;

    public StorageMySql(JpaEntityInformation<E, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        // Assert.notNull(entityInformation, "JpaEntityInformation must not be null!");
        // Assert.notNull(entityManager, "EntityManager must not be null!");
        // this.entityInformation = entityInformation;
        // this.em = entityManager;
        // this.provider = PersistenceProvider.fromEntityManager(entityManager);
    }

    @Override
    public E create(PK id, EntityCreator<PK, E> entityCreator) {
        return null;
    }

    @Override
    public E get(PK id) {
        return null;
    }

    @Override
    public E getByUnique(String name, Object value) {
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
    public void invalidate(PK id) {

    }
}
