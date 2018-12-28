package com.mmorpg.mbdl.bussiness.object.creator;

import com.mmorpg.mbdl.bussiness.object.model.SceneObjectType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 对象生成器管理器
 *
 * @author Sando Geek
 * @since v1.0 2018/12/28
 **/
@Component
public class ObjectCreatorManager {
    private Map<SceneObjectType, AbstractObjectCreator> type2CreatorMap = new HashMap<>(8);
    private static ObjectCreatorManager self;

    @PostConstruct
    private void init() {
        self = this;
    }

    public static ObjectCreatorManager getInstance() {
        return self;
    }

    public void register(AbstractObjectCreator abstractObjectCreator) {
        type2CreatorMap.put(abstractObjectCreator.getObjectType(),abstractObjectCreator);
    }
    public AbstractObjectCreator getCreatorByObjectType(SceneObjectType sceneObjectType){
        return this.type2CreatorMap.get(sceneObjectType);
    }
}
