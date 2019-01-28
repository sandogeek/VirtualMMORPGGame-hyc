package com.mmorpg.mbdl.business.container.model;

import com.google.common.collect.Iterables;
import com.mmorpg.mbdl.TestWithSpring;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

class ContainerTest extends TestWithSpring {
    private static Logger logger = LoggerFactory.getLogger(ContainerTest.class);

    @Test
    void 添加key不在集合中的物品() {
        Container container = new Container();
        container.addItem(1,199);
        Collection<Item> items = container.getId2ItemMap().values();
        Item lastItem = Iterables.getLast(items);
        Assertions.assertEquals(3,items.size());
        Assertions.assertEquals(1,lastItem.getAmount());
    }

    @Test
    void 在原本有key的情况下添加物品但不增加物品id数() {
        Container container = new Container();
        container.addItem(1,199);
        container.addItem(1,20);
        Collection<Item> items = container.getId2ItemMap().values();
        Item lastItem = Iterables.getLast(items);
        Assertions.assertEquals(3,items.size());
        Assertions.assertEquals(21,lastItem.getAmount());
    }

    @Test
    void 在原本有key的情况下添加物品并增加物品id数() {
        Container container = new Container();
        container.addItem(1,199);
        container.addItem(1,120);
        Collection<Item> items = container.getId2ItemMap().values();
        Item lastItem = Iterables.getLast(items);
        Assertions.assertEquals(4,items.size());
        Assertions.assertEquals(22,lastItem.getAmount());
    }

    @Test
    void 前三个物品刚好满的() {
        Container container = new Container();
        container.addItem(1,3*99);
        container.addItem(1,20);
        Collection<Item> items = container.getId2ItemMap().values();
        Item lastItem = Iterables.getLast(items);
        Assertions.assertEquals(4,items.size());
        Assertions.assertEquals(20,lastItem.getAmount());
    }
}