package com.mmorpg.nb.bussiness.command.packet;

import com.mmorpg.nb.bussiness.common.websocketpacket.WebSocketPacketID;

/**
 * 命令执行请求包
 * @author sando
 */
public class CommandExecuteReq {
    private int ID = WebSocketPacketID.COMMAND_EXECUTE_REQ;
    private String executeCommand;
}
