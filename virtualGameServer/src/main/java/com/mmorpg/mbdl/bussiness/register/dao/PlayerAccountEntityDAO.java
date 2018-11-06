package com.mmorpg.mbdl.bussiness.register.dao;

import com.mmorpg.mbdl.bussiness.register.entity.PlayerAccountEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author sando
 */
public interface PlayerAccountEntityDAO extends CrudRepository<PlayerAccountEntity, Long> {

}
