package com.mmorpg.mbdl.GenericTypeLearn;

import java.lang.reflect.Field;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.Collection;

public class GenericTypeTest {
    Collection<String> info;
    public static void main(String[] args) throws Exception{
        // https://stackoverflow.com/questions/39309074/what-represents-reflection-typevariable-interface
        Class clazz = Info.class;
        TypeVariable[] typeParameters = clazz.getTypeParameters();
        GenericDeclaration genericDeclaration = typeParameters[0].getGenericDeclaration();
        Field var = GenericTypeTest.class.getDeclaredField("info");
        ParameterizedType parameterizedType = (ParameterizedType) var.getGenericType();

        System.out.println(typeParameters[0]);
    }
}
