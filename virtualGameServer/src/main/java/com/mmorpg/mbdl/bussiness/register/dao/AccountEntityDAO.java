package com.mmorpg.mbdl.bussiness.register.dao;

import com.mmorpg.mbdl.bussiness.register.entity.AccountEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author sando
 */
public interface AccountEntityDAO extends CrudRepository<AccountEntity, Long> {

}
