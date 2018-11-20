package com.mmorpg.mbdl;

import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.logging.Level;

/**
 * 带Spring的单元测试
 *
 * @author Sando Geek
 * @since v1.0
 **/
@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class TestWithSpring {
    @BeforeAll
    public static void dummy() {
        java.util.logging.Logger sysLogger = java.util.logging.Logger.getLogger(ProtobufProxy.class.getName());
        sysLogger.setLevel(Level.WARNING);
    }
}
