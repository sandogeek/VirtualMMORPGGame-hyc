package com.mmorpg.mbdl.framework.storage.config.JetCache;

import com.mmorpg.mbdl.TestWithSpring;
import com.mmorpg.mbdl.bussiness.register.entity.PlayerAccountEntity;
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
    IStorage<String, PlayerAccountEntity> iStorage;
    @Test
    void create() {
        String account = "sandoTestCreate";
        Assertions.assertThrows(DataIntegrityViolationException.class,()->{
            iStorage.create(account,(id)->{
                PlayerAccountEntity playerAccountEntity = new PlayerAccountEntity();
                // playerAccountEntity.setPlayerId(1222L);
                playerAccountEntity.setAccount(id);
                playerAccountEntity.setPassword("123556");
                return playerAccountEntity;
            });
        });
    }

    PlayerAccountEntity createNotExists(){
        String account = "createNotExsist";
        iStorage.remove(account);
        PlayerAccountEntity playerAccountEntity1 = iStorage.create(account, (id) -> {
            PlayerAccountEntity playerAccountEntity = new PlayerAccountEntity();
            // playerAccountEntity.setPlayerId(1223L);
            playerAccountEntity.setAccount(id);
            playerAccountEntity.setPassword("123556");
            return playerAccountEntity;
        });
        Assertions.assertEquals(account,playerAccountEntity1.getAccount());
        return playerAccountEntity1;
    }

    @Test
    void createWithUniqueConfilict(){
        String account = "createWithUniqueConfilict";
        iStorage.remove(account);
        EntityCreator<String,PlayerAccountEntity> entityEntityCreator = (accountName) -> {
            PlayerAccountEntity playerAccountEntity = new PlayerAccountEntity();
            // playerAccountEntity.setPlayerId(1024L);
            playerAccountEntity.setAccount(accountName);
            playerAccountEntity.setPassword("123556");
            return playerAccountEntity;
        };
        PlayerAccountEntity playerAccountEntity1 = iStorage.create(account, entityEntityCreator);
        Assertions.assertThrows(DataIntegrityViolationException.class,()->{
            PlayerAccountEntity playerAccountEntity2 = iStorage.create(account +1, entityEntityCreator);
        });
    }

    @Test
    void update() {
        PlayerAccountEntity playerAccountEntity = createNotExists();
        playerAccountEntity.setPassword("passWordUpdate");
        iStorage.update(playerAccountEntity);
        Assertions.assertEquals("passWordUpdate",iStorage.get(playerAccountEntity.getAccount()).getPassword());
    }

    @Test
    void get() {
        String account = "createNotExsist";
        PlayerAccountEntity playerAccountEntity = createNotExists();
        Assertions.assertEquals(playerAccountEntity,iStorage.get(account));
    }

    @Test
    void getOrCreate() {
        String account = "getOrCreate";
        iStorage.remove(account);
        PlayerAccountEntity accountEntity = iStorage.getOrCreate(account, (id) -> {
            PlayerAccountEntity playerAccountEntity = new PlayerAccountEntity();
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