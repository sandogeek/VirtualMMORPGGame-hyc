package com.mmorpg.mbdl.business.common.condition;

import com.mmorpg.mbdl.framework.resource.exposed.ResPostProcessor;
import org.reflections.ReflectionUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 带Conditions字段的静态资源的后处理器
 *
 * @author Sando Geek
 * @since v1.0 2019/4/2
 **/
@Component
public class ResWithConditionsPostProcessor implements ResPostProcessor {
    private static Map<Class<?>, Set<Field>> class2Fields = new ConcurrentHashMap<>(8);

    @SuppressWarnings("unchecked")
    @Override
    public void postProcess(Object obj) {
        Set<Field> allFields = class2Fields.computeIfAbsent(obj.getClass(), (clz) -> {
            Set<Field> fields = ReflectionUtils.getAllFields(clz, ReflectionUtils.withTypeAssignableTo(Conditions.class));
            for (Field field : fields) {
                field.setAccessible(true);
            }
            return fields;
        });
        for (Field field : allFields) {
            try {
                Conditions<ICondition<?>> conditions = (Conditions<ICondition<?>>) field.get(obj);
                // 对于同一类资源对象是串行的，所以check是串行的
                conditions.conditionList.forEach(ICondition::check);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
