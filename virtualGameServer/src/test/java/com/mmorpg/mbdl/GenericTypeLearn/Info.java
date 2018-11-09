package com.mmorpg.mbdl.GenericTypeLearn;

public class Info<K extends Number>{	// 指定上限，只能是数字类型
    private K var ;		// 此类型由外部决定
    public K getVar(){
        return this.var ;
    }
    public void setVar(K var){
        this.var = var ;
    }
    public String toString(){		// 覆写Object类中的toString()方法
        return this.var.toString() ;
    }
}
