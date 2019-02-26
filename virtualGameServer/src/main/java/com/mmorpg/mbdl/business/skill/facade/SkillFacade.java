package com.mmorpg.mbdl.business.skill.facade;

import com.mmorpg.mbdl.business.skill.packet.UseSkillReq;
import com.mmorpg.mbdl.business.skill.service.SkillService;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketHandler;
import com.mmorpg.mbdl.framework.communicate.websocket.model.ISession;

/**
 * 技能门面
 *
 * @author Sando Geek
 * @since v1.0 2019/1/28
 **/
@PacketHandler
public class SkillFacade {

    public void handleUseItemReq(ISession session, UseSkillReq useSkillReq) {
        if (useSkillReq.getSkillId()< 1) {
            return;
        }
        if (useSkillReq.getObjId() < 1) {
            return;
        }
        SkillService.getInstance().handleUseSkillReq(session,useSkillReq);
    }
}
