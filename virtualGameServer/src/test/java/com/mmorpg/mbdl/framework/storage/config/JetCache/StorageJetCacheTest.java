package com.mmorpg.mbdl.framework.storage.config.JetCache;

import com.mmorpg.mbdl.TestWithSpring;
import com.mmorpg.mbdl.business.container.entity.ContainerEntity;
import com.mmorpg.mbdl.business.container.model.Container;
import com.mmorpg.mbdl.business.container.model.ContainerType;
import com.mmorpg.mbdl.business.container.model.Item;
import com.mmorpg.mbdl.business.register.entity.AccountEntity;
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
    @Autowired
    private IStorage<Long, ContainerEntity> containerEntityIStorage;
    @Test
    void create() throws Exception {
        String account = "sandoTestCreate";
        AccountEntity accountEntity = new AccountEntity();
        // accountEntity.setRoleId(1222L);
        accountEntity.setAccount(account);
        accountEntity.setPassword("123556");
        Assertions.assertThrows(DataIntegrityViolationException.class,()->{
            iStorage.create(accountEntity);
        });
        System.in.read();
    }

    AccountEntity createNotExists(){
        String account = "createNotExsist";
        iStorage.remove(account);
        AccountEntity accountEntity = new AccountEntity();
        // accountEntity.setRoleId(1223L);
        accountEntity.setAccount(account);
        accountEntity.setPassword("123556");
        AccountEntity accountEntity1 = iStorage.create(accountEntity);
        Assertions.assertEquals(account, accountEntity1.getAccount());
        return accountEntity1;
    }

    @Test
    void createWithUniqueConfilict(){
        String account = "createWithUniqueConfilict";
        iStorage.remove(account);
        AccountEntity accountEntity = new AccountEntity();
        // accountEntity.setRoleId(1024L);
        accountEntity.setAccount(account);
        accountEntity.setPassword("123556");
        iStorage.create(accountEntity);

        AccountEntity accountEntity2 = new AccountEntity();
        // accountEntity.setRoleId(1024L);
        accountEntity.setAccount(account+1);
        accountEntity.setPassword("123556");

        Assertions.assertThrows(DataIntegrityViolationException.class,()->{
            iStorage.create(accountEntity2);
        });
    }

    @Test
    void update() {
        AccountEntity accountEntity = createNotExists();
        accountEntity.setPassword("passWordUpdate");
        iStorage.update(accountEntity);
        iStorage.remove(accountEntity.getId());
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
            // playerAccountEntity.setRoleId(86L);
            playerAccountEntity.setAccount(id);
            playerAccountEntity.setPassword("123556");
            return playerAccountEntity;
        });
        Assertions.assertEquals(iStorage.get(account),accountEntity);
    }

    @Test
    void getOrCreateContainerEntity() {
        containerEntityIStorage.remove(1000L);
        ContainerEntity containerEntity = containerEntityIStorage.getOrCreate(1000L, id -> {
            ContainerEntity entity = new ContainerEntity().setRoleId(id);
            // 赠送一点背包物品
            Container packContainer = new Container();
            // 放入5个小血瓶、5个小蓝瓶
            packContainer.addItem(new Item(1,5));
            packContainer.addItem(new Item(2,5));
            entity.getType2ContainerMap().put(ContainerType.PACK,packContainer);
            return entity;
        });
        containerEntityIStorage.remove(1000L);
    }

    @Test
    void 更新不存在的实体() {
        ContainerEntity entity = new ContainerEntity().setRoleId(1000L);
        // 赠送一点背包物品
        Container packContainer = new Container();
        // 放入5个小血瓶、5个小蓝瓶
        packContainer.addItem(new Item(1,5));
        packContainer.addItem(new Item(2,5));
        entity.getType2ContainerMap().put(ContainerType.PACK,packContainer);
        ContainerEntity updatedEntity = containerEntityIStorage.update(entity);
        containerEntityIStorage.remove(1000L);
        logger.debug("");
    }

    @Test
    void remove() {
        iStorage.remove("createNotExsist");
    }

}