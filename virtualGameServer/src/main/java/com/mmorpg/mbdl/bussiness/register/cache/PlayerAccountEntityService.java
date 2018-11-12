package com.mmorpg.mbdl.bussiness.register.cache;

import com.github.xiaolyuh.annotation.CacheEvict;
import com.github.xiaolyuh.annotation.CachePut;
import com.github.xiaolyuh.annotation.Cacheable;
import com.mmorpg.mbdl.bussiness.register.entity.PlayerAccountEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author sando
 */
@Component
public class PlayerAccountEntityService {
    Logger logger = LoggerFactory.getLogger(PlayerAccountEntityService.class);

    // @Autowired
    // private PlayerAccountEntityDAO jpaRepository;
    @Autowired
    private JpaRepository<PlayerAccountEntity, Long> jpaRepository;

    @CachePut(value = "player.account", key = "#playerAccount.playerId", depict = "用户信息缓存")
    public PlayerAccountEntity saveAndFlush(PlayerAccountEntity playerAccount) {
        logger.info("为id、key为:" + playerAccount.getPlayerId() + "数据做了缓存");
        return jpaRepository.saveAndFlush(playerAccount);
    }

    @CacheEvict(value = "player.account", key = "#id")
    public void delete(Long id) {
        logger.info("删除了id、key为" + id + "的数据缓存");
        jpaRepository.delete(id);
    }

    @CacheEvict(value = "player.account", allEntries = true)
    public void invalidateAll() {
        logger.info("删除了所有缓存的数据缓存");
    }

    @Cacheable(value = "player.account", key = "#id", depict = "用户信息缓存")
    public PlayerAccountEntity get(Long id) {
        logger.info("为id、key为:" + id + "数据做了缓存");
        return jpaRepository.findOne(id);
    }
}
