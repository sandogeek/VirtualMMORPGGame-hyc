package com.mmorpg.mbdl.bussiness.world.model;

/**
 * 怪物出生的特殊信息，公共信息可以通过objectKey在其它静态资源中找到
 *
 * @author Sando Geek
 * @since v1.0 2018/12/6
 **/
public class MonsterSpawn {
    private int objectKey;
    private String name;

    public int getObjectKey() {
        return objectKey;
    }

    public String getName() {
        return name;
    }
}
