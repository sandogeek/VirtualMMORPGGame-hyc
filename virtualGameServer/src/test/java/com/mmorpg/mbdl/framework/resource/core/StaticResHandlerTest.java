package com.mmorpg.mbdl.framework.resource.core;

import com.mmorpg.mbdl.TestWithSpring;
import com.mmorpg.mbdl.bussiness.common.GlobalSettingRes;
import com.mmorpg.mbdl.bussiness.common.error.resource.ErrorTipsRes;
import com.mmorpg.mbdl.bussiness.item.resource.ItemRes;
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
    private IStaticRes<String, ItemRes> itemReses;
    @Autowired
    private IStaticRes<String, GlobalSettingRes> globalSettingResIStaticRes;

    @Test
    void 静态资源注入测试() {
        Assertions.assertNotNull(errorTipsReses);
        Assertions.assertNotNull(itemReses);
        Assertions.assertNotNull(globalSettingResIStaticRes);
        logger.debug("{}", itemReses.get("iron1000").getId());
    }

}