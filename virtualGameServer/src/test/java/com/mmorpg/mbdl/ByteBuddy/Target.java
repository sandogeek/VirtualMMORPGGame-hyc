package com.mmorpg.mbdl.ByteBuddy;

import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.Super;
import net.bytebuddy.implementation.bind.annotation.This;

/**
 * 目标类
 *
 * @author Sando Geek
 * @since v1.0 2018/12/5
 **/
public class Target {
    int k = 2;
    public String hello(@This ISource source, @Argument(0) String name) {
        // @This,所以访问的是增强后的方法，所以anInt应该为4
        int anInt = source.getInt();
        return "Hello " + name + "! int= "+anInt+" k = "+k;
    }
    public int getInt(@Super ISource source) throws Exception {
        return 2+source.a;
    }

    public int myMethod(int a){
        return a;
    }

    public int test() {
        return 4;
    }
    // public static String intercept(Object o) { return o.toString(); }
}
