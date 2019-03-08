package com.mmorpg.mbdl.business.skill.packet.vo;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 * 可用技能前端信息
 *
 * @author Sando Geek
 * @since v1.0 2019/3/8
 **/
public class SkillUiInfo {
    @Protobuf(description = "技能id", required = true)
    private int skillId;
    @Protobuf(description = "技能名称", required = true)
    private String skillName;

    public SkillUiInfo() {
    }

    public SkillUiInfo(int skillId, String skillName) {
        this.skillId = skillId;
        this.skillName = skillName;
    }
}
