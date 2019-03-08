package com.mmorpg.mbdl.business.skill.manager;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.embedded.CaffeineCacheBuilder;
import com.mmorpg.mbdl.business.common.IRoleEntityManager;
import com.mmorpg.mbdl.business.common.packet.GlobalMessage;
import com.mmorpg.mbdl.business.common.packet.vo.GlobalMessageType;
import com.mmorpg.mbdl.business.object.model.AbstractCreature;
import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.business.role.model.prop.PropType;
import com.mmorpg.mbdl.business.skill.entity.SkillEntity;
import com.mmorpg.mbdl.business.skill.packet.SkillListUpdate;
import com.mmorpg.mbdl.business.skill.packet.vo.SkillUiInfo;
import com.mmorpg.mbdl.business.skill.res.SkillRes;
import com.mmorpg.mbdl.business.skill.util.GameMathUtil;
import com.mmorpg.mbdl.framework.resource.exposed.IStaticRes;
import com.mmorpg.mbdl.framework.storage.core.IStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

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
    /**
     * 角色id -> map 技能key -> 技能使用毫秒
     * 技能最长cd不超过20分钟
     */
    private Cache<Long, ConcurrentHashMap<Integer, Long>> cache = CaffeineCacheBuilder.createCaffeineCacheBuilder()
            .expireAfterAccess(20, TimeUnit.MINUTES)
            .loader((key -> new ConcurrentHashMap(16)))
            .buildCache();

    @PostConstruct
    private void init() {
        self = this;
    }

    public static SkillManager getInstance() {
        return self;
    }

    public void useSkill(Role role, SkillRes skillRes, AbstractCreature target) {
        // 检查cd
        ConcurrentHashMap<Integer, Long> key2lastUseTime = cache.get(role.getRoleId());
        Long lastUseTime = key2lastUseTime.get(skillRes.getSkillId());
        if (lastUseTime != null && (System.currentTimeMillis() - lastUseTime < skillRes.getCd())) {
            role.sendPacket(new GlobalMessage(GlobalMessageType.ERROR, String.format("[%s]技能还在cd中", skillRes.getSkillName())));
            return;
        }
        int mpCost = skillRes.getMpCost();
        long damage = GameMathUtil.computeDamage(skillRes.getBasicDamage(),
                (int) role.getPropManager().getPropValueOf(PropType.ATTACK),
                skillRes.getAttackPercent(),
                (int) target.getPropManager().getPropValueOf(PropType.DEFENCE));
        // TODO 目前没有其它途径消耗蓝，如果有要注意是否会出现线程安全问题,可能需要把后续判断等操作放到回调中，以便上锁
        long currentMp = role.getPropManager().getPropValueOf(PropType.CURRENT_MP);
        if (mpCost > currentMp) {
            role.sendPacket(new GlobalMessage(GlobalMessageType.ERROR, "蓝量不足,技能释放失败"));
            return;
        }
        long currentHp = role.getPropManager().getPropValueOf(PropType.CURRENT_HP);
        if (damage < currentHp) {
            target.changeHp(-damage);
        } else {
            target.changeHp(-currentHp);
        }

        role.changeMp(-mpCost);
        // 给目标造成的血量扣除 {"skillId":2,"targetId":183887031360888321}
        role.broadcast(new GlobalMessage(String.format("玩家[%s]对[%s]造成%s伤害", role.getName(), target.getName(), damage)), true);
        key2lastUseTime.put(skillRes.getSkillId(), System.currentTimeMillis());
    }

    public SkillRes getSkillResById(int skillId) {
        return skillId2SkillRes.get(skillId);
    }

    public boolean containsSkillId(int skillId) {
        return skillId2SkillRes.containsKey(skillId);
    }

    @Override
    public void bindEntity(Role role) {
        SkillEntity entity = skillEntityIStorage.getOrCreate(role.getRoleId(), SkillEntity::new);
        entity.setOwner(role);
        role.setSkillEntity(entity);
        // 发送技能列表信息
        List<SkillUiInfo> skillUiInfoList = new ArrayList<>(4);
        for (SkillRes skillRes : skillId2SkillRes.values()) {
            skillUiInfoList.add(new SkillUiInfo(skillRes.getSkillId(), skillRes.getSkillName()));
        }
        role.sendPacket(new SkillListUpdate(skillUiInfoList));
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
