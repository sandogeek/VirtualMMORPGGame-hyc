package com.mmorpg.mbdl.business.world.resource;

import com.mmorpg.mbdl.business.world.model.BornData;
import com.mmorpg.mbdl.framework.resource.annotation.Key;
import com.mmorpg.mbdl.framework.resource.annotation.ResDef;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.List;

/**
 * 怪物出生静态资源
 *
 * @author Sando Geek
 * @since v1.0 2018/12/6
 **/
@ResDef(suffix = ".json")
public class BornRes {
    @Key
    private int sceneId;
    private List<BornData> bornDataList;

    public int getSceneId() {
        return sceneId;
    }

    @SuppressFBWarnings("UWF_UNWRITTEN_FIELD")
    public List<BornData> getBornDataList() {
        return bornDataList;
    }
}
