package com.mmorpg.mbdl.bussiness.container.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mmorpg.mbdl.framework.resource.annotation.Id;
import com.mmorpg.mbdl.framework.resource.annotation.ResDef;

/**
 * 物品资源
 *
 * @author Sando Geek
 * @since v1.0
 **/
@ResDef
public class ItemRes13 {
    @Id
    @JsonProperty(value = "Id")
    private String id;

    @JsonProperty(value = "Name")
    private String name;

    @JsonProperty(value = "Quality")
    private int quality;

    @JsonProperty(value = "CanUse")
    private boolean canUse;

    @JsonProperty(value = "Type")
    private int type;

    @JsonProperty(value = "Func")
    private int func;

    @JsonProperty(value = "Parameter1")
    private int parameter1;

    @JsonProperty(value = "Parameter2")
    private int parameter2;

    @JsonProperty(value = "Parameter3")
    private int parameter3;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuality() {
        return quality;
    }

    public boolean isCanUse() {
        return canUse;
    }

    public int getType() {
        return type;
    }

    public int getFunc() {
        return func;
    }

    public int getParameter1() {
        return parameter1;
    }

    public int getParameter2() {
        return parameter2;
    }

    public int getParameter3() {
        return parameter3;
    }
}
