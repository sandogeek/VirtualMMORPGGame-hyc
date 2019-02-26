package com.mmorpg.mbdl.business.skill.res;

import com.mmorpg.mbdl.framework.resource.annotation.Key;
import com.mmorpg.mbdl.framework.resource.annotation.ResDef;

/**
 * 技能资源
 *
 * @author Sando Geek
 * @since v1.0 2019/2/26
 **/
@ResDef
public class SkillRes {
    /**
     * 技能id
     */
    @Key
    private int skillId;
    /**
     * 技能名称
     */
    private String skillName;
    /**
     * 技能伤害
     */
    private int basicDamage;
    /**
     * cd 单位ms
     */
    private int cd;
    /**
     * 蓝耗
     */
    private int mpCost;
    /**
     * 攻击加成百分比
     */
    private short attackPercent;

    public int getSkillId() {
        return skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public int getBasicDamage() {
        return basicDamage;
    }

    public int getCd() {
        return cd;
    }

    public int getMpCost() {
        return mpCost;
    }

    public short getAttackPercent() {
        return attackPercent;
    }
}
