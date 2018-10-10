package com.mmorpg.nb.bussiness.command.model;

/**
 * 命令类型，每种类型对于一个AbstractCommand对象
 * @author sando
 */
public enum CommandType {
    /**
     * 场景间移动命令
     */
    MOVE("场景间移动命令");

    /**
     * 命令描述，可能在命令行帮助和浏览器显示时使用
      */
    private String desc;
    CommandType(String desc){
        this.desc=desc;
    }
}
