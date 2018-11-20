package com.mmorpg.mbdl.ReflectASM;

public class UserService {
    int state;
    public int update(int n,String a){
        int hash = n + a.hashCode();
        return hash;
    }
}
