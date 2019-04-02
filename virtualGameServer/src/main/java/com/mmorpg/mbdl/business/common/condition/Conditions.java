package com.mmorpg.mbdl.business.common.condition;

import java.util.List;

/**
 * 条件集合，为了保证{@link ICondition#check()}生效，其对象必须在{@link com.mmorpg.mbdl.framework.resource.annotation.ResDef}
 * 标注的类中生成，通常在afterLoad中生成
 *
 * @author Sando Geek
 * @since v1.0 2019/3/29
 **/
public class Conditions<T> {
    List<ICondition<T>> conditionList;

    public Conditions() {
    }

    public Conditions(List<ICondition<T>> conditionList) {
        this.conditionList = conditionList;
    }

    /**
     * 验证条件集合是否全部通过
     *
     * @param obj 验证的主体
     * @return 全部通过返回true，只要有一个失败就返回false
     */
    public boolean verifyAnd(T obj) {
        for (ICondition<T> condition : conditionList) {
            if (!condition.verify(obj)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证条件集合是否有其中一个通过
     *
     * @param obj 验证的主体
     * @return 只要有一个通过返回true，全部失败就返回false
     */
    public boolean verifyOr(T obj) {
        for (ICondition<T> condition : conditionList) {
            if (condition.verify(obj)) {
                return true;
            }
        }
        return false;
    }

}
