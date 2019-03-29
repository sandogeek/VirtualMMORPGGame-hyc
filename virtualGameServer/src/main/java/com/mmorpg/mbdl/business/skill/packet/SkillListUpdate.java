package com.mmorpg.mbdl.business.skill.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.common.PacketIdManager;
import com.mmorpg.mbdl.business.skill.packet.vo.SkillUiInfo;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

import java.util.List;

/**
 * 技能列表更新
 *
 * @author Sando Geek
 * @since v1.0 2019/3/8
 **/
@ProtoDesc(description = "技能列表更新")
public class SkillListUpdate extends AbstractPacket {
    @Protobuf(description = "技能列表信息", required = true)
    private List<SkillUiInfo> skillUiInfoList;

    public SkillListUpdate() {
    }

    public SkillListUpdate(List<SkillUiInfo> skillUiInfoList) {
        this.skillUiInfoList = skillUiInfoList;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.SKILL_LIST_UPDATE;
    }
}