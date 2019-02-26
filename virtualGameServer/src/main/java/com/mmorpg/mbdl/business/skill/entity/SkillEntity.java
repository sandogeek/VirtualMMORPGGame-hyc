package com.mmorpg.mbdl.business.skill.entity;

import com.mmorpg.mbdl.business.common.orm.JsonType;
import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.business.skill.manager.SkillManager;
import com.mmorpg.mbdl.framework.storage.annotation.JetCacheConfig;
import com.mmorpg.mbdl.framework.storage.core.AbstractEntity;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 技能实体
 *
 * @author Sando Geek
 * @since v1.0 2019/2/26
 **/
@Entity
@JetCacheConfig
public class SkillEntity extends AbstractEntity<Long> {
    private transient Role owner;
    @Id
    private Long roleId;
    /**
     * 技能id -> 技能上次使用时间(毫秒)
     */
    @Type(type = JsonType.NAME)
    private ConcurrentHashMap<Integer,Long> skillId2time = new ConcurrentHashMap<>();

    public SkillEntity setOwner(Role owner) {
        this.owner = owner;
        return this;
    }

    /**
     * 设置技能上次使用时间
     * @param skillId
     * @param lastUseTime
     */
    public void setSkillLastUseTime(int skillId, long lastUseTime) {
        SkillManager instance = SkillManager.getInstance();
        if (!instance.containsSkillId(skillId)) {
            return;
        }
        skillId2time.put(skillId, lastUseTime);
        instance.mergeUpdateEntity(this);
    }

    @Override
    public Long getId() {
        return roleId;
    }
}