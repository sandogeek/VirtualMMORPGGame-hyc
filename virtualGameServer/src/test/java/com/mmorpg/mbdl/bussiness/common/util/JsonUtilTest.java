package com.mmorpg.mbdl.bussiness.common.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class JsonUtilTest {
    private static Logger logger = LoggerFactory.getLogger(JsonUtilTest.class);
    @Test
    void 序列化() {
        String s = JsonUtil.object2String(new JsonTest());
        logger.debug(s);
    }
}