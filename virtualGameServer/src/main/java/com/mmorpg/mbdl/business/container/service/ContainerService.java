package com.mmorpg.mbdl.business.container.service;

import com.mmorpg.mbdl.business.container.manager.ContainerManager;
import com.mmorpg.mbdl.business.container.model.AbstractItem;
import com.mmorpg.mbdl.business.container.model.Container;
import com.mmorpg.mbdl.business.container.model.ContainerType;
import com.mmorpg.mbdl.business.container.model.handler.ItemUseHandlerManager;
import com.mmorpg.mbdl.business.container.packet.GetPackContentReq;
import com.mmorpg.mbdl.business.container.packet.GetPackContentResp;
import com.mmorpg.mbdl.business.container.packet.UseItemReq;
import com.mmorpg.mbdl.business.container.packet.UseItemResp;
import com.mmorpg.mbdl.business.container.packet.VO.ItemUiInfo;
import com.mmorpg.mbdl.business.container.res.ItemRes;
import com.mmorpg.mbdl.business.role.event.RoleLogoutEvent;
import com.mmorpg.mbdl.business.role.manager.RoleManager;
import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.framework.communicate.websocket.model.ISession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

/**
 * 容器服务
 *
 * @author Sando Geek
 * @since v1.0 2019/1/15
 **/
@Component
public class ContainerService {
    private static ContainerService self;

    @Autowired
    private ContainerManager containerManager;

    @PostConstruct
    private void init() {
        self = this;
    }

    public static ContainerService getInstance() {
        return self;
    }

    public void handleRoleLogoutEvent(RoleLogoutEvent roleLogoutEvent) {
        ContainerManager.getInstance().updateEntity(roleLogoutEvent.getRole().getContainerEntity());
    }

    public void handleGetPackContentReq(ISession session, GetPackContentReq getPackContentReq) {
        Role role = RoleManager.getInstance().getRoleBySession(session);
        Container packContainer = role.getContainerEntity().getType2ContainerMap().get(ContainerType.PACK);
        Collection<AbstractItem> abstractItems = packContainer.getAll();
        GetPackContentResp getPackContentResp = new GetPackContentResp();
        List<ItemUiInfo> itemUiInfoList = getPackContentResp.getItemUiInfoList();
        for (AbstractItem abstractItem : abstractItems) {
            ItemRes itemRes = containerManager.getItemResByKey(abstractItem.getKey());
            ItemUiInfo itemUiInfo = new ItemUiInfo(abstractItem.getObjectId(), abstractItem.getKey(),
                    itemRes.getName(), abstractItem.getAmount(), itemRes.getItemType());
            itemUiInfoList.add(itemUiInfo);
        }
        role.sendPacket(getPackContentResp);
    }

    public void handleUseItemReq(ISession session, UseItemReq useItemReq) {
        Role role = RoleManager.getInstance().getRoleBySession(session);
        UseItemResp useItemResp = new UseItemResp();
        Container packContainer = role.getContainerEntity().getType2ContainerMap().get(ContainerType.PACK);
        // 只有物品最大堆叠数为1时物品使用请求提供ObjectId
        if (useItemReq.getObjectId() != 0) {
            AbstractItem abstractItem = packContainer.getItemByObjectId(useItemReq.getObjectId());
            if (abstractItem == null) {
                throw new RuntimeException(String.format("找不到objectId为%s的物品",useItemReq.getObjectId()));
            }
            ItemRes itemRes = ContainerManager.getInstance().getItemResByKey(abstractItem.getKey());
            if (itemRes.getMaxAmount() != 1) {
                throw new RuntimeException("提供objectId的物品最大堆叠数不为1");
            }
            boolean result = ItemUseHandlerManager.getInstance().
                    getItemUseHandlerByType(abstractItem.getItemType())
                    .useById(role, packContainer, abstractItem, itemRes, useItemReq.getObjectId());
            useItemResp.setResult(result);
        } else {
            ItemRes itemRes = ContainerManager.getInstance().getItemResByKey(useItemReq.getKey());
            int amountLeft = packContainer.getAmountByKey(useItemReq.getKey());
            if (useItemReq.getAmount() > amountLeft) {
                throw new RuntimeException("剩余物品数量不足");
            }
            boolean result = ItemUseHandlerManager.getInstance().getItemUseHandlerByType(itemRes.getItemType())
                    .useByKey(role, packContainer, useItemReq.getKey(), useItemReq.getAmount(), itemRes);
            useItemResp.setResult(result);
        }
        role.sendPacket(useItemResp);
    }
}
