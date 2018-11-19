package com.mmorpg.mbdl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 带Spring的单元测试
 *
 * @author Sando Geek
 * @since v1.0
 **/
@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class TestWithSpring {
}
