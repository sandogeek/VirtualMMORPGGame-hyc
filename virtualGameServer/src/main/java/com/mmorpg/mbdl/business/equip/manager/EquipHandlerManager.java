package com.mmorpg.mbdl.business.equip.manager;

import com.mmorpg.mbdl.business.equip.model.EquipType;
import com.mmorpg.mbdl.business.equip.model.handler.AbstractEquipHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 装备处理器管理器
 *
 * @author Sando Geek
 * @since v1.0 2019/2/18
 **/
@Component
public class EquipHandlerManager {
    private static EquipHandlerManager self;

    private Map<EquipType, AbstractEquipHandler> type2EquipHandlerMap = new HashMap<>(4);

    @PostConstruct
    private void init() {
        self = this;
    }

    public static EquipHandlerManager getInstance() {
        return self;
    }

    public void register(AbstractEquipHandler abstractEquipHandler) {
        if (type2EquipHandlerMap.get(abstractEquipHandler.getEquipType()) != null) {
            throw new RuntimeException("装备处理器重复");
        }
        type2EquipHandlerMap.put(abstractEquipHandler.getEquipType(), abstractEquipHandler);
    }

    public AbstractEquipHandler getEquipHandlerByEquipType(EquipType equipType) {
        return type2EquipHandlerMap.get(equipType);
    }
}
