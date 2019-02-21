package com.mmorpg.mbdl.business.container.packet.VO;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.business.container.manager.ContainerManager;
import com.mmorpg.mbdl.business.container.model.ItemType;
import com.mmorpg.mbdl.business.container.res.ItemRes;

/**
 * 物品前端能拿到的信息
 *
 * @author Sando Geek
 * @since v1.0 2019/1/29
 **/
public class ItemUiInfo {
    @Protobuf(description = "物品唯一id",required = true)
    private long objectId;
    @Protobuf(description = "对应物品表中的key", required = true)
    private int key;
    @Protobuf(description = "名称", required = true)
    private String name;
    @Protobuf(description = "数量",required = true)
    private int amount;
    @Protobuf(description = "物品类型",required = true)
    private ItemType itemType;

    public ItemUiInfo() {
    }

    public ItemUiInfo(long objectId, int key, int amount) {
        this.objectId = objectId;
        this.key = key;
        this.amount = amount;
        ItemRes itemResByKey = ContainerManager.getInstance().getItemResByKey(key);
        this.name = itemResByKey.getName();
        this.itemType = itemResByKey.getItemType();
    }

    public ItemUiInfo(long objectId, int key, String name, int amount, ItemType itemType) {
        this.objectId = objectId;
        this.key = key;
        this.name = name;
        this.amount = amount;
        this.itemType = itemType;
    }
}
