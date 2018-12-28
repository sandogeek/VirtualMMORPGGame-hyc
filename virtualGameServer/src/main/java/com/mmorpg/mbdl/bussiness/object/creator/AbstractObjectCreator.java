package com.mmorpg.mbdl.bussiness.object.creator;

import com.mmorpg.mbdl.bussiness.object.model.AbstractVisibleSceneObject;
import com.mmorpg.mbdl.bussiness.object.model.SceneObjectType;
import com.mmorpg.mbdl.bussiness.world.model.BornData;
import com.mmorpg.mbdl.bussiness.world.resource.SceneObjectAttrRes;
import com.mmorpg.mbdl.framework.resource.exposed.IStaticRes;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * 抽象对象生成器
 *
 * @author Sando Geek
 * @since v1.0 2018/12/28
 **/
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
