package com.mmorpg.mbdl.framework.resource.core;

import com.mmorpg.mbdl.TestWithSpring;
import com.mmorpg.mbdl.business.common.resource.GlobalSettingRes;
import com.mmorpg.mbdl.business.common.resource.ErrorTipsRes;
import com.mmorpg.mbdl.business.world.resource.BornRes;
import com.mmorpg.mbdl.framework.resource.exposed.IStaticRes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

class StaticResHandlerTest extends TestWithSpring {
    private static Logger logger = LoggerFactory.getLogger(StaticResHandlerTest.class);
    @Autowired
    private IStaticRes<Integer, ErrorTipsRes> errorTipsReses;
    @Autowired
    private IStaticRes<String, GlobalSettingRes> globalSettingResIStaticRes;
    @Autowired
    private IStaticRes<Integer, BornRes> monsterSpawnResIStaticRes;

    @Test
    void 静态资源注入测试() {
        Assertions.assertNotNull(errorTipsReses);
        Assertions.assertNotNull(globalSettingResIStaticRes);
        Assertions.assertNotNull(monsterSpawnResIStaticRes);
        logger.debug("{}", monsterSpawnResIStaticRes.get(1001).getBornDataList());
    }

}