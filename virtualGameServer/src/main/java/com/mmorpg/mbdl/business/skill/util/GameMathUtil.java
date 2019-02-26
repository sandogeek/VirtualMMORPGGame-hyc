package com.mmorpg.mbdl.business.skill.util;

/**
 * 游戏中的简单数学运算
 *
 * @author Sando Geek
 * @since v1.0 2019/2/26
 **/
public class GameMathUtil {
    /**
     * 计算被攻击目标受到的伤害
     * @return
     */
    public static long computeDamage(int basicDamage,int attack,int attackPercent,int defence) {
        return ((long) ((basicDamage + attack * attackPercent / 100.0) * defence / (100.0 + defence)));
    }
}
