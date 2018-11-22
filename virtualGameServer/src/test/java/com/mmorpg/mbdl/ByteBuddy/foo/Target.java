package com.mmorpg.mbdl.ByteBuddy.foo;

import net.bytebuddy.asm.Advice;

/**
 * @author Sando Geek
 * @since v1.0
 **/
public class Target {
    public static String hello(@Advice.This Source source, @Advice.Argument(0) String name) {
        // 访问的是增强后的方法，所以anInt应该为2
        int anInt = source.getInt();
        return "Hello " + name + "! int= "+anInt;
    }
    public static int getInt() {
        return 2;
    }
    // public static String intercept(Object o) { return o.toString(); }
}
