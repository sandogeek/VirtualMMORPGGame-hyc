package com.mmorpg.mbdl.common.jsonserializer;

import com.mmorpg.mbdl.business.role.model.RoleType;
import com.mmorpg.mbdl.framework.common.utils.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EnumReadableSerializerTest {
    private static Logger logger = LoggerFactory.getLogger(EnumReadableSerializerTest.class);
    private static class CustomSerializeTest {
        private RoleType roleType;

        public CustomSerializeTest() {
        }

        public CustomSerializeTest(RoleType roleType) {
            this.roleType = roleType;
        }

        public RoleType getRoleType() {
            return roleType;
        }
    }

    @Test
    void 测试序列化() {
        CustomSerializeTest customSerializeTest = new CustomSerializeTest(RoleType.SAINT);
        String s = JsonUtil.object2String(customSerializeTest);
        logger.debug(s);
    }

    @Test
    void 测试反序列化() {
        CustomSerializeTest customSerializeTest = JsonUtil.string2Object("{\"roleType\":3}", CustomSerializeTest.class);
        Assertions.assertEquals(RoleType.SAINT,customSerializeTest.getRoleType());
    }
}