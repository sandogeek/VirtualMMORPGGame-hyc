package com.mmorpg.mbdl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackTest {
    private static Logger log = LoggerFactory.getLogger(LogbackTest.class);
    public static void main(String[] args) {
        log.debug("======debug");
        log.info("======info");
        log.warn("======warn");
        log.error("======error");

        String name = "Aub";
        String message = "3Q";
        String[] fruits = { "apple", "banana" };

        // logback提供的可以使用变量的打印方式，结果为"Hello,Aub!"
        log.debug("Hello,{}!", name);

        // 可以有多个参数,结果为“Hello,Aub! 3Q!”
        log.info("Hello,{}!   {}!", name, message);

        // 可以传入一个数组，结果为"Fruit:  apple,banana"
        log.warn("Fruit:  {},{}", fruits);
    }
}
