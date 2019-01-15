package com.mmorpg.mbdl.bussiness.container.service;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 容器服务
 *
 * @author Sando Geek
 * @since v1.0 2019/1/15
 **/
@Component
public class ContainerService {
    private static ContainerService self;

    @PostConstruct
    private void init() {
        self = this;
    }

    public static ContainerService getInstance() {
        return self;
    }
}
