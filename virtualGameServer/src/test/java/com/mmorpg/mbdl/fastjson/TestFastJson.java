package com.mmorpg.mbdl.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmorpg.mbdl.framework.reflectasm.withunsafe.FieldAccess;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试类
 *
 * @author Sando Geek
 * @since v1.0 2018/12/3
 **/
public class TestFastJson {
    private static Logger logger = LoggerFactory.getLogger(TestFastJson.class);
    @Test
    void 枚举转换测试() {
        FieldAccess javaBeanTestAccess = FieldAccess.accessUnsafe(JavaBeanTest.class);
        FieldAccess javaBean2Access = FieldAccess.accessUnsafe(JavaBean2.class);
        JavaBeanTest javaBeanTest = new JavaBeanTest();
        JavaBean2 javaBean2 = new JavaBean2();
        javaBeanTestAccess.setInt(javaBeanTest,javaBeanTestAccess.getIndex("anInt"),15);
        javaBeanTestAccess.setObject(javaBeanTest,"state",State.LOGIN);
        javaBean2Access.setInt(javaBean2,javaBeanTestAccess.getIndex("anInt"),25);
        javaBean2Access.setObject(javaBean2,"state",State.ONLINE);
        javaBeanTestAccess.setObject(javaBeanTest,"javaBean2",javaBean2);
        javaBeanTestAccess.setObject(javaBeanTest,"string","hello json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            String s = mapper.writeValueAsString(javaBeanTest);
            logger.info("jackson:{}",s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        JavaBeanTest test = JSON.parseObject("{\"string\":\"hello json\",\"anInt\":15,\"state\":\"LOGIN\"}", JavaBeanTest.class, Feature.SupportNonPublicField);
        logger.info("{}", JSON.toJSON(javaBeanTest, new SerializeConfig(true)));

    }
}
