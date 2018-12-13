package com.mmorpg.mbdl.bussiness.register.cache;

import com.mmorpg.mbdl.bussiness.register.entity.AccountEntity;
import com.mmorpg.mbdl.framework.common.generator.IdGenerator;
import com.mmorpg.mbdl.framework.common.generator.IdGeneratorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Random;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:applicationContext.xml")
class AccountEntityServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(AccountEntityServiceTest.class);
    // @Autowired
    // PlayerAccountEntityService playerAccountEntityService;

    @Test
    void save() {
        long playerId = 1222L;
        long playerId2 = 10001L;
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccount("sando"+ new Random().nextInt(300));
        accountEntity.setPassword("123556");
        IdGenerator idGenerator = IdGeneratorFactory.getIntance().getRoleIdGenerator();
        // accountEntity.setPlayerId(playerId);
        for (int i=0;i<5;i++){
            try {
                // playerAccountEntityService.saveAndFlush(accountEntity);
                // playerAccountEntityService.saveAndFlush(playerAccountEntity2);
                break;
            }catch (Exception e){
            }
        }

        // AccountEntity playerAccountEntity1 = playerAccountEntityService.get(playerId);
        // Assert.assertEquals(new Long(playerId), playerAccountEntity1.getPlayerId());
    }

    @Test
    void delete() throws Exception {
        long playerId = 10001L;
        AccountEntity accountEntity2 = new AccountEntity();
        accountEntity2.setAccount("sando"+ new Random().nextInt(300));
        accountEntity2.setPassword("123556");
        // IdGenerator idGenerator = IdGeneratorFactory.getInstance().getRoleIdGenerator();
        // accountEntity2.setPlayerId(playerId);
        // playerAccountEntityService.saveAndFlush(accountEntity2);
        Thread.sleep(5000);
        // playerAccountEntityService.delete(playerId);
        // Assertions.assertThrows(EmptyResultDataAccessException.class,()->{
        //     AccountEntity accountEntity = playerAccountEntityService.get(playerId);
        //     logger.info("{}",accountEntity);
        // });
        // Assertions.assertNull(playerAccountEntityService.get(playerId));
    }

    @Test
    void invalidatAll() {
        // playerAccountEntityService.invalidateAll();
    }

    @Test
    void findOne() {
    }
}