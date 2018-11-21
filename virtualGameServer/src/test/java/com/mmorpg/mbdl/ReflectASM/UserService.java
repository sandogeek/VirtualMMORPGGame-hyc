package com.mmorpg.mbdl.ReflectASM;

public class UserService {
    private int statePrivate;
    int state;
    Integer stateInteger;
    private static long longStatic;
    public int update(int n,String a){
        int hash = n + a.hashCode();
        return hash;
    }

    public static long getLongStatic() {
        return longStatic;
    }

    public int getState() {
        return state;
    }

    public int getStatePrivate() {
        return statePrivate;
    }

    public Integer getStateInteger() {
        return stateInteger;
    }
}
