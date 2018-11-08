package com.mmorpg.mbdl.bussiness.register.cache;

import com.mmorpg.mbdl.bussiness.register.entity.PlayerAccountEntity;
import com.mmorpg.mbdl.framework.communicate.websocket.generator.IdGenerator;
import com.mmorpg.mbdl.framework.communicate.websocket.generator.IdGeneratorFactory;
import junit.framework.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:applicationContext.xml")
class PlayerAccountEntityServiceTest {
    @Autowired
    PlayerAccountEntityService playerAccountEntityService;

    @Test
    void save() {
        PlayerAccountEntity playerAccountEntity = new PlayerAccountEntity();
        playerAccountEntity.setAccount("sando1");
        playerAccountEntity.setPassword("123556");
        IdGenerator idGenerator = IdGeneratorFactory.getIntance().getPlayerIdGenerator();
        long playerId = idGenerator.generate();
        playerAccountEntity.setPlayerId(playerId);

        playerAccountEntityService.save(playerAccountEntity);
        PlayerAccountEntity playerAccountEntity1 = playerAccountEntityService.findOne(playerAccountEntity);
        Assert.assertEquals(new Long(playerId), playerAccountEntity1.getPlayerId());
    }

    @Test
    void remove() {
    }

    @Test
    void removeAll() {
    }

    @Test
    void findOne() {
    }
}