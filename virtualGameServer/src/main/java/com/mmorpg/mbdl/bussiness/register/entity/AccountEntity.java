package com.mmorpg.mbdl.bussiness.register.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AccountEntity {
    @Id
    private Long playerId;
    @Column(unique = true)
    private String account;
    @Column
    private String password;
}
