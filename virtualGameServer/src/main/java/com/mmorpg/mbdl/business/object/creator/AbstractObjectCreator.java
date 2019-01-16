package com.mmorpg.mbdl.business.object.creator;

import com.mmorpg.mbdl.business.object.model.AbstractVisibleSceneObject;
import com.mmorpg.mbdl.business.object.model.SceneObjectType;
import com.mmorpg.mbdl.business.world.model.BornData;
import com.mmorpg.mbdl.business.world.resource.SceneObjectAttrRes;
import com.mmorpg.mbdl.framework.resource.exposed.IStaticRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 抽象对象生成器
 *
 * @author Sando Geek
 * @since v1.0 2018/12/28
 **/
@Component
public abstract class AbstractObjectCreator<T extends AbstractVisibleSceneObject> {
    @Autowired
    private ObjectCreatorManager objectCreatorManager;
    @Autowired
    protected IStaticRes<Integer, SceneObjectAttrRes> sceneObjectAttrResMap;

    @PostConstruct
    private void register(){
        objectCreatorManager.register(this);
    }

    /**
     * 获取场景对象类型
     * @return 场景对象类型
     */
    public abstract SceneObjectType getObjectType();

    /**
     * 创建场景对象
     * @param sceneId 场景id
     * @param bornData 出生数据
     * @return
     */
    public abstract T create(int sceneId,BornData bornData);
}
