package com.mmorpg.mbdl.business.container.entity;

import com.mmorpg.mbdl.business.common.orm.JsonType;
import com.mmorpg.mbdl.business.container.model.Container;
import com.mmorpg.mbdl.business.container.model.ContainerType;
import com.mmorpg.mbdl.framework.storage.annotation.JetCacheConfig;
import com.mmorpg.mbdl.framework.storage.core.AbstractEntity;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;

/**
 * 背包实体
 *
 * @author Sando Geek
 * @since v1.0 2019/1/15
 **/
@Entity
@JetCacheConfig
public class ContainerEntity extends AbstractEntity<Long> {
    @Id
    private Long roleId;
    @Type(type = JsonType.NAME)
    private Map<ContainerType, Container> type2ContainerMap = new HashMap<>(8);

    public Long getRoleId() {
        return roleId;
    }

    public ContainerEntity setRoleId(Long roleId) {
        this.roleId = roleId;
        return this;
    }

    public Map<ContainerType, Container> getType2ContainerMap() {
        return type2ContainerMap;
    }

    @Override
    public Long getId() {
        return roleId;
    }
}