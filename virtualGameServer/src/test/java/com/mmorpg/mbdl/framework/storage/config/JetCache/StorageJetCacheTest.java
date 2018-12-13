package com.mmorpg.mbdl.framework.storage.config.JetCache;

import com.mmorpg.mbdl.TestWithSpring;
import com.mmorpg.mbdl.bussiness.register.entity.AccountEntity;
import com.mmorpg.mbdl.framework.storage.core.EntityCreator;
import com.mmorpg.mbdl.framework.storage.core.IStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;


class StorageJetCacheTest extends TestWithSpring {
    private static final Logger logger = LoggerFactory.getLogger(StorageJetCache.class);
    @Autowired
    IStorage<String, AccountEntity> iStorage;
    @Test
    void create() {
        String account = "sandoTestCreate";
        Assertions.assertThrows(DataIntegrityViolationException.class,()->{
            iStorage.create(account,(id)->{
                AccountEntity accountEntity = new AccountEntity();
                // accountEntity.setPlayerId(1222L);
                accountEntity.setAccount(id);
                accountEntity.setPassword("123556");
                return accountEntity;
            });
        });
    }

    AccountEntity createNotExists(){
        String account = "createNotExsist";
        iStorage.remove(account);
        AccountEntity accountEntity1 = iStorage.create(account, (id) -> {
            AccountEntity accountEntity = new AccountEntity();
            // accountEntity.setPlayerId(1223L);
            accountEntity.setAccount(id);
            accountEntity.setPassword("123556");
            return accountEntity;
        });
        Assertions.assertEquals(account, accountEntity1.getAccount());
        return accountEntity1;
    }

    @Test
    void createWithUniqueConfilict(){
        String account = "createWithUniqueConfilict";
        iStorage.remove(account);
        EntityCreator<String, AccountEntity> entityEntityCreator = (accountName) -> {
            AccountEntity accountEntity = new AccountEntity();
            // accountEntity.setPlayerId(1024L);
            accountEntity.setAccount(accountName);
            accountEntity.setPassword("123556");
            return accountEntity;
        };
        AccountEntity accountEntity1 = iStorage.create(account, entityEntityCreator);
        Assertions.assertThrows(DataIntegrityViolationException.class,()->{
            AccountEntity accountEntity2 = iStorage.create(account +1, entityEntityCreator);
        });
    }

    @Test
    void update() {
        AccountEntity accountEntity = createNotExists();
        accountEntity.setPassword("passWordUpdate");
        iStorage.update(accountEntity);
        Assertions.assertEquals("passWordUpdate",iStorage.get(accountEntity.getAccount()).getPassword());
    }

    @Test
    void get() {
        String account = "createNotExsist";
        AccountEntity accountEntity = createNotExists();
        Assertions.assertEquals(accountEntity,iStorage.get(account));
    }

    @Test
    void getOrCreate() {
        String account = "getOrCreate";
        iStorage.remove(account);
        AccountEntity accountEntity = iStorage.getOrCreate(account, (id) -> {
            AccountEntity playerAccountEntity = new AccountEntity();
            // playerAccountEntity.setPlayerId(86L);
            playerAccountEntity.setAccount(id);
            playerAccountEntity.setPassword("123556");
            return playerAccountEntity;
        });
        Assertions.assertEquals(iStorage.get(account),accountEntity);
    }

    @Test
    void remove() {
        iStorage.remove("createNotExsist");
    }

}