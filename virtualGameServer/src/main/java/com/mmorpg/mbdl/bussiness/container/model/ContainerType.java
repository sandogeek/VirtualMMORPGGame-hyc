package com.mmorpg.mbdl.bussiness.container.model;

/**
 * 容器类型
 *
 * @author Sando Geek
 * @since v1.0 2019/1/15
 **/
public enum ContainerType {

    BAG("背包");

    private String desc;

    ContainerType(String desc) {
        this.desc = desc;
    }
}
