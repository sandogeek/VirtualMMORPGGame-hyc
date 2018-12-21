package com.mmorpg.mbdl.framework.storage.core;

import com.mmorpg.mbdl.bussiness.register.entity.AccountEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:applicationContext.xml")
class StorageLayringCacheTest {
    private static final Logger logger = LoggerFactory.getLogger(StorageLayringCacheTest.class);
    @Autowired
    IStorage<String, AccountEntity> iStorage;

    @Test
    void create() throws IOException {
        // AccountEntity fromCache = iStorage.getFromCache(1222L, AccountEntity.class);
        // iStorage.create(1222L,(id)->{
        //     AccountEntity playerAccountEntity = new AccountEntity();
        //     playerAccountEntity.setRoleId(id);
        //     playerAccountEntity.setAccount("sando"+ new Random().nextInt(300));
        //     playerAccountEntity.setPassword("123556");
        //     return playerAccountEntity;
        // });
        // AccountEntity fromCache1 = iStorage.getFromCache(1222L, AccountEntity.class);
        // System.in.read();
        // logger.info("zenn");

    }

    @Test
    void get() {
    }

    @Test
    void update() {
    }

    @Test
    void remove() {
    }

    @Test
    void invalidate() {
    }

    @Test
    void setApplicationContext() {
    }
}