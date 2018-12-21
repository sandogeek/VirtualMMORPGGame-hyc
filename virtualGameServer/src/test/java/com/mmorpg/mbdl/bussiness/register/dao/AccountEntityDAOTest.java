package com.mmorpg.mbdl.bussiness.register.dao;

import com.mmorpg.mbdl.bussiness.register.entity.AccountEntity;
import com.mmorpg.mbdl.framework.common.generator.IdGenerator;
import com.mmorpg.mbdl.framework.common.generator.IdGeneratorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:applicationContext.xml")
class AccountEntityDAOTest {
    @Autowired
    private JpaRepository<AccountEntity,Long> playerAccountEntityDAO;

    @Test
    void accountEntity() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccount("sando3");
        accountEntity.setPassword("123456");
        IdGenerator idGenerator = IdGeneratorFactory.getIntance().getRoleIdGenerator();
        // accountEntity.setRoleId(idGenerator.generate());
        playerAccountEntityDAO.save(accountEntity);
    }
}