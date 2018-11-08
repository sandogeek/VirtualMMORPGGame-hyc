package com.mmorpg.mbdl.bussiness.register.dao;

import com.mmorpg.mbdl.bussiness.register.entity.PlayerAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author sando
 */
public interface PlayerAccountEntityDAO extends JpaRepository<PlayerAccountEntity, Long> {

}
