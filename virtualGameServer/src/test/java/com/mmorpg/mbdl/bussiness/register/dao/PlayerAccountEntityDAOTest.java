package com.mmorpg.mbdl.bussiness.register.dao;

import com.mmorpg.mbdl.bussiness.register.entity.PlayerAccountEntity;
import com.mmorpg.mbdl.framework.common.generator.IdGenerator;
import com.mmorpg.mbdl.framework.common.generator.IdGeneratorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:applicationContext.xml")
class PlayerAccountEntityDAOTest {
    @Autowired
    private PlayerAccountEntityDAO playerAccountEntityDAO;

    @Test
    void accountEntity() {
        PlayerAccountEntity playerAccountEntity = new PlayerAccountEntity();
        playerAccountEntity.setAccount("sando3");
        playerAccountEntity.setPassword("123456");
        IdGenerator idGenerator = IdGeneratorFactory.getIntance().getPlayerIdGenerator();
        playerAccountEntity.setPlayerId(idGenerator.generate());
        playerAccountEntityDAO.save(playerAccountEntity);
    }
}