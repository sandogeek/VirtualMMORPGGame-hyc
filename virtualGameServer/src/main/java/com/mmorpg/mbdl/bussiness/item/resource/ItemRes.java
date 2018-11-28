package com.mmorpg.mbdl.bussiness.item.resource;

import com.mmorpg.mbdl.framework.resource.annotation.Attr;
import com.mmorpg.mbdl.framework.resource.annotation.Id;
import com.mmorpg.mbdl.framework.resource.annotation.ResDef;

/**
 * 物品资源
 *
 * @author Sando Geek
 * @since v1.0
 **/
@ResDef
public class ItemRes {
    @Id
    @Attr(value = "Id")
    private int id;

    @Attr(value = "Name")
    private String name;

    @Attr(value = "Quality")
    private int quality;

    @Attr(value = "CanUse")
    private boolean canUse;

    @Attr(value = "Type")
    private int type;

    @Attr(value = "Func")
    private int func;

    @Attr(value = "Name")
    private int parameter1;

    @Attr(value = "Parameter2")
    private int parameter2;

    @Attr(value = "Parameter3")
    private int parameter3;

}
