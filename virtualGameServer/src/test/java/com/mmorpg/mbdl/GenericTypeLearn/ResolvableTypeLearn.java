package com.mmorpg.mbdl.GenericTypeLearn;

import org.junit.jupiter.api.Test;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.util.List;

public class ResolvableTypeLearn {
    List<List<? extends Number>> listsNumber;
    List<List<Long>> listsLong;
    List<List<Object>> listsObject;

    @Test
    public void isAssinabelFromTest(){
        // http://jinnianshilongnian.iteye.com/blog/1993608
        ResolvableType resolvableTypeListsNumber =
                ResolvableType.forField(ReflectionUtils.findField(ResolvableTypeLearn.class, "listsNumber"));
        ResolvableType resolvableTypeListsLong =
                ResolvableType.forField(ReflectionUtils.findField(ResolvableTypeLearn.class, "listsLong"));
        ResolvableType resolvableTypeListsObject =
                ResolvableType.forField(ReflectionUtils.findField(ResolvableTypeLearn.class, "listsObject"));
        Assert.isTrue(resolvableTypeListsNumber.isAssignableFrom(resolvableTypeListsLong),"可以赋值");
        Assert.isTrue(!resolvableTypeListsNumber.isAssignableFrom(resolvableTypeListsObject),"不可赋值");
    }

}
