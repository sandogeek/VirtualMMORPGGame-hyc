package com.mmorpg.mbdl.bussiness.object.model;

/**
 * 可见物
 *
 * @author Sando Geek
 * @since v1.0 2018/12/11
 **/
public abstract class AbstractVisibleObject extends AbstractObject {
    /**
     * 所在的场景id
     */
    private int mapId;

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }
}
