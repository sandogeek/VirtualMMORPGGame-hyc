package com.mmorpg.mbdl.bussiness.container.entity;

import com.mmorpg.mbdl.bussiness.container.model.Container;
import com.mmorpg.mbdl.bussiness.container.model.ContainerType;
import com.mmorpg.mbdl.framework.storage.annotation.JetCacheConfig;
import com.mmorpg.mbdl.framework.storage.core.IEntity;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Map;

/**
 * 背包实体
 *
 * @author Sando Geek
 * @since v1.0 2019/1/15
 **/
@Entity
@JetCacheConfig
public class ContainerEntity implements IEntity<Long> {
    @Id
    private Long roleId;
    @Type(type = "json")
    private Map<ContainerType, Container> type2ContainerMap;

    public ContainerEntity(Long roleId, Map<ContainerType, Container> type2ContainerMap) {
        this.roleId = roleId;
        this.type2ContainerMap = type2ContainerMap;
    }

    @Override
    public Long getId() {
        return roleId;
    }
}