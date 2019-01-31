package com.mmorpg.mbdl.business.container.model.creator;

import com.mmorpg.mbdl.business.container.model.ItemType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 物品创建器管理器
 *
 * @author Sando Geek
 * @since v1.0 2019/1/30
 **/
@Component
public class ItemCreatorManager {
    private static ItemCreatorManager self;

    private Map<ItemType, AbstractItemCreator> type2AbstractItemCreatorMap = new HashMap<>();

    @PostConstruct
    private void init() {
        self = this;
    }

    public static ItemCreatorManager getInstance() {
        return self;
    }

    public void register(AbstractItemCreator abstractItemCreator) {
        if (type2AbstractItemCreatorMap.get(abstractItemCreator.getItemType())!=null){
            throw new RuntimeException(String.format("%s重复",AbstractItemCreator.class.getSimpleName()));
        }
        type2AbstractItemCreatorMap.put(abstractItemCreator.getItemType(),abstractItemCreator);
    }

    public AbstractItemCreator getCreatorByItemType(ItemType itemType) {
        return type2AbstractItemCreatorMap.get(itemType);
    }
}
