package com.mmorpg.mbdl.business.container.model.handler;

import com.mmorpg.mbdl.business.container.model.ItemType;
import com.mmorpg.mbdl.business.container.model.creator.AbstractItemCreator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 物品使用处理器管理器
 *
 * @author Sando Geek
 * @since v1.0 2019/2/15
 **/
@Component
public class ItemUseHandlerManager {
    private static ItemUseHandlerManager self;

    private Map<ItemType, AbstractItemUseHandler> type2AbstractItemCreatorMap = new HashMap<>();

    @PostConstruct
    private void init() {
        self = this;
    }

    public static ItemUseHandlerManager getInstance() {
        return self;
    }

    protected void register(AbstractItemUseHandler abstractItemUseHandler) {
        if (type2AbstractItemCreatorMap.get(abstractItemUseHandler.getItemType())!=null){
            throw new RuntimeException(String.format("%s重复", AbstractItemCreator.class.getSimpleName()));
        }
        type2AbstractItemCreatorMap.put(abstractItemUseHandler.getItemType(),abstractItemUseHandler);
    }

    public AbstractItemUseHandler getItemUseHandlerByType(ItemType itemType) {
        return type2AbstractItemCreatorMap.get(itemType);
    }
}
