package com.mmorpg.mbdl.business.skill.service;

import com.mmorpg.mbdl.business.object.model.AbstractCreature;
import com.mmorpg.mbdl.business.object.model.AbstractVisibleSceneObject;
import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.business.role.model.prop.PropType;
import com.mmorpg.mbdl.business.skill.manager.SkillManager;
import com.mmorpg.mbdl.business.skill.packet.UseSkillReq;
import com.mmorpg.mbdl.business.skill.res.SkillRes;
import com.mmorpg.mbdl.business.world.manager.SceneManager;
import com.mmorpg.mbdl.business.world.scene.model.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 技能服务
 *
 * @author Sando Geek
 * @since v1.0 2019/1/28
 **/
@Component
public class SkillService {
    private static SkillService self;

    @Autowired
    private SkillManager skillManager;

    @PostConstruct
    private void init() {
        self = this;
    }

    public static SkillService getInstance() {
        return self;
    }

    public void handleUseSkillReq(Role role, UseSkillReq useSkillReq) {
        long sceneId = role.getPropManager().getPropValueOf(PropType.SCENE_ID);
        Scene scene = SceneManager.getInstance().getSceneBySceneId((int) sceneId);
        AbstractVisibleSceneObject visibleObj = scene.getVisibleObjById(useSkillReq.getTargetId());
        if (visibleObj == null) {
            return;
        }

        if (!(visibleObj instanceof AbstractCreature)) {
            return;
        }

        SkillRes skillRes = skillManager.getSkillResById(useSkillReq.getSkillId());
        if (skillRes == null) {
            return;
        }
        skillManager.useSkill(role, skillRes, (AbstractCreature) visibleObj);
    }
}
