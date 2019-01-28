package com.mmorpg.mbdl.business.skill.service;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 技能服务
 *
 * @author Sando Geek
 * @since v1.0 2019/1/28
 **/
@Component
public class SkillService {
    private static SkillService self;

    @PostConstruct
    private void init() {
        self = this;
    }

    public static SkillService getInstance() {
        return self;
    }
}
