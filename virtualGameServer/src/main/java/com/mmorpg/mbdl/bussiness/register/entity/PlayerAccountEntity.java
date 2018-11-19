package com.mmorpg.mbdl.bussiness.register.entity;

import com.mmorpg.mbdl.framework.storage.annotation.JetCacheConfig;
import com.mmorpg.mbdl.framework.storage.annotation.LayeringCacheConfig;
import com.mmorpg.mbdl.framework.storage.core.IEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@LayeringCacheConfig(depict = "玩家账户")
@JetCacheConfig
public class PlayerAccountEntity implements IEntity<String> {
    @Id
    private String account;
    @Column(unique = true)
    private Long playerId;
    @Column
    private String password;

    @Override
    public String getId() {
        return account;
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
