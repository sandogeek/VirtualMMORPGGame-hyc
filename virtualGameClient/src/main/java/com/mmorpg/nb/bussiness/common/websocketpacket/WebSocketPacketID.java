package com.mmorpg.nb.bussiness.common.websocketpacket;

public interface WebSocketPacketID {
    // 命令执行请求
    short COMMAND_EXECUTE_REQ=10001;
    // 命令执行结果响应
    short COMMAND_RESULT_RESP=10002;
    // 对象增加推送包
    short OBJECT_ADD_PUSH=10101;
    // 对象更新推送包
    short OBJECT_UPDATE_PUSH=10102;
    // 对象删除推送包
    short OBJECT_DELETE_PUSH=10103;
    // 初始化动态对象的两个map的请求
    short MAP_INIT_REQ=10104;
    // 初始化动态对象的两个map的响应
    short MAP_INIT_RESP=10105;
}
