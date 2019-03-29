package com.mmorpg.mbdl.business.skill.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

/**
 * 使用技能请求
 *
 * @author Sando Geek
 * @since v1.0 2019/2/26
 **/
@ProtoDesc(description = "使用技能请求")
public class UseSkillReq extends AbstractPacket {
    @Protobuf(required = true, description = "技能id")
    private int skillId;
    @Protobuf(required = true,description = "目标对象的id")
    private long targetId;

    public UseSkillReq() {
    }

    public int getSkillId() {
        return skillId;
    }

    public UseSkillReq setSkillId(int skillId) {
        this.skillId = skillId;
        return this;
    }

    public long getTargetId() {
        return targetId;
    }

    public UseSkillReq setTargetId(long targetId) {
        this.targetId = targetId;
        return this;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.USE_SKILL_REQ;
    }
}