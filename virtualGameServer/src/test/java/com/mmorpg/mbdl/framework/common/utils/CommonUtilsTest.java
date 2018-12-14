package com.mmorpg.mbdl.framework.common.utils;

import com.mmorpg.mbdl.TestWithSpring;
import com.mmorpg.mbdl.framework.common.generator.IdGenerator;
import com.mmorpg.mbdl.framework.common.generator.IdGeneratorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

class CommonUtilsTest extends TestWithSpring {
    private static Logger logger = LoggerFactory.getLogger(CommonUtilsTest.class);
    @Autowired
    private IdGeneratorFactory idGeneratorFactory;

    @Test
    void getSeverTokenById() {
        IdGenerator idGenerator = idGeneratorFactory.getRoleIdGenerator();
        Long generate1 = idGenerator.generate();
        Long generate2 = idGenerator.generate();
        int severTokenById1 = CommonUtils.getSeverTokenById(generate1);
        int severTokenById2 = CommonUtils.getSeverTokenById(generate2);
        logger.debug("{}",Integer.toBinaryString(severTokenById1));
        Assertions.assertEquals(severTokenById1,severTokenById2);
    }
}