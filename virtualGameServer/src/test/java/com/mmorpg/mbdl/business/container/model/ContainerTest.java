package com.mmorpg.mbdl.business.container.model;

import com.google.common.collect.Iterables;
import com.mmorpg.mbdl.TestWithSpring;
import com.mmorpg.mbdl.business.container.entity.ContainerEntity;
import com.mmorpg.mbdl.framework.storage.core.IStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.NoSuchElementException;

class ContainerTest extends TestWithSpring {
    private static Logger logger = LoggerFactory.getLogger(ContainerTest.class);
    @Autowired
    private IStorage<Long, ContainerEntity> containerEntityIStorage;
    private Container container;

    @BeforeEach
    void setUp() {
        containerEntityIStorage.remove(1000L);
        ContainerEntity containerEntity = containerEntityIStorage.getOrCreate(1000L, id -> {
            ContainerEntity entity = new ContainerEntity().setRoleId(id);
            // 赠送一点背包物品
            Container packContainer = new Container();
            // 放入90个小血瓶
            packContainer.createItem(1, 90);
            // 两把木剑
            packContainer.createItem(10000, 2);
            entity.getType2ContainerMap().put(ContainerType.PACK,packContainer);
            return entity;
        });
        container = containerEntity.getType2ContainerMap().get(ContainerType.PACK);
    }

    @Test
    void 添加key不在集合中的物品() {
        Container container = new Container();
        container.createItem(1, 49 * 3 + 1);
        container.createItem(10000, 2);
        Collection<AbstractItem> abstractItems = container.getAll();
        AbstractItem lastAbstractItem = Iterables.getLast(abstractItems);
        Assertions.assertEquals(6, abstractItems.size());
        Assertions.assertEquals(1, lastAbstractItem.getAmount());
    }

    @Test
    void 在原本有key的情况下添加物品但不增加物品id数() {
        Container container = new Container();
        container.createItem(1, 49 * 3 + 1);
        container.createItem(1, 20);
        Collection<AbstractItem> abstractItems = container.getAll();
        AbstractItem lastAbstractItem = Iterables.getLast(abstractItems);
        Assertions.assertEquals(4, abstractItems.size());
        Assertions.assertEquals(21, lastAbstractItem.getAmount());
    }

    @Test
    void 在原本有key的情况下添加物品并增加物品id数() {
        Container container = new Container();
        container.createItem(1, 49 * 3 + 1);
        container.createItem(1, 49 + 21);
        Collection<AbstractItem> abstractItems = container.getAll();
        AbstractItem lastAbstractItem = Iterables.getLast(abstractItems);
        Assertions.assertEquals(5, abstractItems.size());
        Assertions.assertEquals(22, lastAbstractItem.getAmount());
    }

    @Test
    void 前三个物品刚好满的() {
        Container container = new Container();
        container.createItem(1, 3 * 49);
        container.createItem(1, 20);
        Collection<AbstractItem> abstractItems = container.getAll();
        AbstractItem lastAbstractItem = Iterables.getLast(abstractItems);
        Assertions.assertEquals(4, abstractItems.size());
        Assertions.assertEquals(20, lastAbstractItem.getAmount());
    }

    @Test
    void 删除物品() {
        container.removeItem(1,49+41);
        Collection<AbstractItem> abstractItems = container.getAll();
        Assertions.assertThrows(NoSuchElementException.class, () -> Iterables.getLast(abstractItems));
        container.createItem(1, 49 + 41);
    }


}