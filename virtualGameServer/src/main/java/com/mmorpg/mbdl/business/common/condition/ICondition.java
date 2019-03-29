package com.mmorpg.mbdl.business.common.condition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mmorpg.mbdl.business.common.condition.impl.role.RoleLevelCondition;

/**
 * 条件接口
 * TODO 利用注解自动生成描述以及使用模板,并自动检测变更来决定是否更新说明（利用在生成的文件中记录MD5的方式）<br/>
 * 如：<br/>
 * 总说明：角色等级最低达到min级，最高达到max级，则条件验证成功<br/>
 * 模板：<br/>
 * <pre>
 *     {"@type": "roleLevel", "min": , "max": }
 * </pre>
 *
 * @author Sando Geek
 * @since v1.0 2019/3/29
 **/
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RoleLevelCondition.class, name = "roleLevel"),
})
public interface ICondition<T> {
    /**
     * 验证条件是否通过
     *
     * @param obj 验证的主体对象
     * @return 条件通过返回true，否则返回false TODO 返回值变成对象，记录条件未通过的原因
     */
    boolean verify(T obj);

    /**
     * 检查配置的条件有无异常
     */
    void check();
}
