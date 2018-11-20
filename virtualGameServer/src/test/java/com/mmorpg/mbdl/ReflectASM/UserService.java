package com.mmorpg.mbdl.ReflectASM;

public class UserService {
    private int statePrivate;
    int state;
    public int update(int n,String a){
        int hash = n + a.hashCode();
        return hash;
    }

    public int getState() {
        return state;
    }

    public int getStatePrivate() {
        return statePrivate;
    }
}
