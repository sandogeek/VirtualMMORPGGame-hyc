package com.mmorpg.mbdl.business.register.service;

import com.mmorpg.mbdl.business.register.entity.AccountEntity;
import com.mmorpg.mbdl.business.register.packet.RegisterReq;
import com.mmorpg.mbdl.business.register.packet.RegisterResp;
import com.mmorpg.mbdl.framework.common.generator.IdGeneratorFactory;
import com.mmorpg.mbdl.framework.storage.core.IStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 注册服务
 *
 * @author Sando Geek
 * @since v1.0 2018/12/7
 **/
@Component
public class RegisterService {
    @Autowired
    private IStorage<String, AccountEntity> playerAccountEntityIStorage;
    @Autowired
    private IdGeneratorFactory idGeneratorFactory;

    public RegisterResp register(RegisterReq registerReq){
        RegisterResp registerResp = new RegisterResp();
       if (playerAccountEntityIStorage.get(registerReq.getAccount())!=null){
           registerResp.setSuccess(false);
       }else {
           AccountEntity accountEntity = new AccountEntity();
           accountEntity.setAccount(registerReq.getAccount());
           accountEntity.setPassword(registerReq.getPassword());
           playerAccountEntityIStorage.create(accountEntity);
           registerResp.setSuccess(true);
       }
       return registerResp;
    }
}
