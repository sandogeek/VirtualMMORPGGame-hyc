package com.mmorpg.mbdl.bussiness.register.dao;

import com.mmorpg.mbdl.bussiness.register.entity.AccountEntity;
import com.mmorpg.mbdl.framework.communicate.websocket.generator.IdGenerator;
import com.mmorpg.mbdl.framework.communicate.websocket.generator.IdGeneratorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:applicationContext.xml")
class AccountEntityDAOTest {
    @Autowired
    private AccountEntityDAO accountEntityDAO;

    @Test
    void accountEntity() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccount("sando3");
        accountEntity.setPassword("123456");
        IdGenerator idGenerator = IdGeneratorFactory.getIntance().getPlayerIdGenerator();
        accountEntity.setPlayerId(idGenerator.generate());
        accountEntityDAO.save(accountEntity);
    }
}