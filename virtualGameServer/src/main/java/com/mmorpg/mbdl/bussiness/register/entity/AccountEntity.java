package com.mmorpg.mbdl.bussiness.register.entity;

import com.mmorpg.mbdl.framework.storage.annotation.JetCacheConfig;
import com.mmorpg.mbdl.framework.storage.core.IEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author sando
 */
@Entity
@JetCacheConfig
public class AccountEntity implements IEntity<String> {
    @Id
    private String account;

    @Column
    private String password;

    @Override
    public String getId() {
        return account;
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
