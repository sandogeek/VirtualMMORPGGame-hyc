package com.mmorpg.mbdl.bussiness.register.cache;

import com.github.xiaolyuh.annotation.*;
import com.mmorpg.mbdl.bussiness.register.entity.PlayerAccountEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author sando
 */
@Component
public class PlayerAccountEntityService {
    Logger logger = LoggerFactory.getLogger(PlayerAccountEntityService.class);

    @CachePut(value = "player.account", key = "#playerAccount.playerId", depict = "用户信息缓存")
    public PlayerAccountEntity save(PlayerAccountEntity playerAccount) {
        logger.info("为id、key为:" + playerAccount.getPlayerId() + "数据做了缓存");
        return playerAccount;
    }

    @CacheEvict(value = "player.account", key = "#id")//2
    public void remove(Long id) {
        logger.info("删除了id、key为" + id + "的数据缓存");
        //这里不做实际删除操作
    }

    @CacheEvict(value = "player.account", allEntries = true)//2
    public void removeAll() {
        logger.info("删除了所有缓存的数据缓存");
        //这里不做实际删除操作
    }

    @Cacheable(value = "player.account", key = "#playerAccount.playerId", depict = "用户信息缓存")
    public PlayerAccountEntity findOne(PlayerAccountEntity playerAccount) {
        PlayerAccountEntity playerAccountEntity = new PlayerAccountEntity();
        playerAccountEntity.setAccount("sando1");
        playerAccountEntity.setPassword("123556");
        // IdGenerator idGenerator = IdGeneratorFactory.getIntance().getPlayerIdGenerator();
        playerAccountEntity.setPlayerId(3L);
        logger.info("为id、key为:" + playerAccountEntity.getPlayerId() + "数据做了缓存");
        return playerAccountEntity;
    }
}
