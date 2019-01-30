package com.mmorpg.mbdl.business.container.service;

import com.mmorpg.mbdl.business.container.manager.ContainerManager;
import com.mmorpg.mbdl.business.container.model.AbstractItem;
import com.mmorpg.mbdl.business.container.model.Container;
import com.mmorpg.mbdl.business.container.model.ContainerType;
import com.mmorpg.mbdl.business.container.packet.GetPackContentReq;
import com.mmorpg.mbdl.business.container.packet.GetPackContentResp;
import com.mmorpg.mbdl.business.container.packet.UseItemReq;
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
            ItemUiInfo itemUiInfo = new ItemUiInfo(abstractItem.getObjectId(), abstractItem.getKey(), itemRes.getName(), abstractItem.getAmount());
            itemUiInfoList.add(itemUiInfo);
        }
        role.sendPacket(getPackContentResp);
    }

    public void handleUseItemReq(ISession session, UseItemReq useItemReq) {
        Role role = RoleManager.getInstance().getRoleBySession(session);
        if (useItemReq.getObjectId() != 0) {
            Container packContainer = role.getContainerEntity().getType2ContainerMap().get(ContainerType.PACK);
            AbstractItem abstractItem = packContainer.getItemByObjectId(useItemReq.getObjectId());
            if (abstractItem == null) {
                throw new RuntimeException("客户端请求不正确，最大堆叠数不为一的物品使用请求应传入key而不是objectId");
            }
            ItemRes itemRes = ContainerManager.getInstance().getItemResByKey(abstractItem.getKey());

        }
    }
}
