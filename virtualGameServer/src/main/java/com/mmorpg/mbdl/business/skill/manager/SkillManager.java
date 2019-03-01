package com.mmorpg.mbdl.business.skill.manager;

import com.mmorpg.mbdl.business.common.IRoleEntityManager;
import com.mmorpg.mbdl.business.common.packet.GlobalMessage;
import com.mmorpg.mbdl.business.object.model.AbstractCreature;
import com.mmorpg.mbdl.business.role.manager.PropManager;
import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.business.role.model.prop.PropType;
import com.mmorpg.mbdl.business.skill.entity.SkillEntity;
import com.mmorpg.mbdl.business.skill.res.SkillRes;
import com.mmorpg.mbdl.business.skill.util.GameMathUtil;
import com.mmorpg.mbdl.framework.resource.exposed.IStaticRes;
import com.mmorpg.mbdl.framework.storage.core.IStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 技能管理器
 *
 * @author Sando Geek
 * @since v1.0 2019/2/26
 **/
@Component
public class SkillManager implements IRoleEntityManager<SkillEntity> {
    private static SkillManager self;

    @Autowired
    private IStorage<Long,SkillEntity> skillEntityIStorage;

    @Autowired
    private IStaticRes<Integer, SkillRes> skillId2SkillRes;

    @PostConstruct
    private void init() {
        self = this;
    }

    public static SkillManager getInstance() {
        return self;
    }

    public void useSkill(Role role, int skillId, AbstractCreature target) {
        SkillRes skillRes = skillId2SkillRes.get(skillId);
        int mpCost = skillRes.getMpCost();
        PropManager propManager = role.getPropManager();
        long currentMp = propManager.getPropValueOf(PropType.CURRENT_MP);
        if (mpCost > currentMp) {
            role.sendPacket(new GlobalMessage("蓝量不足,技能释放失败"));
            return;
        }
        role.changeMp(-mpCost);
        // 给目标造成的血量扣除
        long hpDown = GameMathUtil.computeDamage(skillRes.getBasicDamage(),
                (int) propManager.getPropValueOf(PropType.ATTACK),
                skillRes.getAttackPercent(),
                (int) target.getPropManager().getPropValueOf(PropType.DEFENCE));

    }

    public boolean containsSkillId(int skillId) {
        return skillId2SkillRes.containsKey(skillId);
    }

    @Override
    public void bindEntity(Role role) {
        SkillEntity entity = skillEntityIStorage.getOrCreate(role.getRoleId(), id -> new SkillEntity(id));
        entity.setOwner(role);
        role.setSkillEntity(entity);
    }

    @Override
    public void updateEntity(SkillEntity entity) {
        skillEntityIStorage.update(entity);
    }

    @Override
    public void mergeUpdateEntity(SkillEntity entity) {
        skillEntityIStorage.mergeUpdate(entity);
    }
}
