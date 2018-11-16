package com.mmorpg.mbdl.bussiness.register.entity;

import com.mmorpg.mbdl.framework.storage.annotation.CacheConfig;
import com.mmorpg.mbdl.framework.storage.core.IEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@CacheConfig(depict = "玩家账户")
public class PlayerAccountEntity implements IEntity<Long> {
    @Id
    private Long playerId;
    @Column(unique = true)
    private String account;
    @Column
    private String password;

    @Override
    public Long getId() {
        return playerId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
