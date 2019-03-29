package com.mmorpg.mbdl.business.common.condition;

import com.mmorpg.mbdl.framework.resource.exposed.IAfterLoad;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * 带条件资源的静态资源
 *
 * @author Sando Geek
 * @since v1.0 2019/3/29
 **/
public class BaseResWithConditions implements IAfterLoad {

    @Override
    public void afterLoad() {
        checkConditions();
    }

    @SuppressWarnings("unchecked")
    private void checkConditions() {
        Set<Field> allFields = ReflectionUtils.getAllFields(this.getClass(), ReflectionUtils.withTypeAssignableTo(Conditions.class));
        for (Field field : allFields) {
            field.setAccessible(true);
            try {
                Conditions<ICondition<?>> conditions = (Conditions<ICondition<?>>) field.get(this);
                conditions.conditionList.forEach(ICondition::check);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
