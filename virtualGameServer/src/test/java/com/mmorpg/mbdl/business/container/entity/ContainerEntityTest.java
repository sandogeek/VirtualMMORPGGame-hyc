package com.mmorpg.mbdl.business.container.entity;

import com.mmorpg.mbdl.TestWithSpring;
import com.mmorpg.mbdl.business.container.model.Container;
import com.mmorpg.mbdl.business.container.model.ContainerType;
import com.mmorpg.mbdl.framework.storage.core.IStorage;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

class ContainerEntityTest extends TestWithSpring {
    private static Logger logger = LoggerFactory.getLogger(ContainerEntityTest.class);
    @Autowired
    private IStorage<Long, ContainerEntity> containerEntityIStorage;

    @Test
    void 使用Type方式入库() {
        containerEntityIStorage.remove(10000L);
        ContainerEntity containerEntity = new ContainerEntity();
        containerEntity.setRoleId(10000L);
        Map<ContainerType, Container> type2ContainerMap = containerEntity.getType2ContainerMap();
        type2ContainerMap.put(ContainerType.BAG,new Container().setContainerType(ContainerType.BAG));
        containerEntityIStorage.create(containerEntity);
    }

    @Test
    void 从数据查取数据() {
        ContainerEntity containerEntity = containerEntityIStorage.get(10000L);
        logger.debug("");
    }
}