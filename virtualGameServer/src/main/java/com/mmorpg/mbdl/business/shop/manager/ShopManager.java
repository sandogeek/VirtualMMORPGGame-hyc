package com.mmorpg.mbdl.business.shop.manager;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 商店管理器
 *
 * @author Sando Geek
 * @since v1.0 2019/2/1
 **/
@Component
public class ShopManager {
    private static ShopManager self;

    @PostConstruct
    private void init() {
        self = this;
    }

    public static ShopManager getInstance() {
        return self;
    }
}
