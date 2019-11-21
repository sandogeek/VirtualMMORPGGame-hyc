package com.mmorpg.mbdl.business.chat.handler;

import com.mmorpg.mbdl.business.chat.model.ChatType;
import com.mmorpg.mbdl.business.chat.packet.ChatReq;
import com.mmorpg.mbdl.business.role.model.Role;

/**
 * 聊天处理器基类
 *
 * @author Sando Geek
 * @since v1.0 2019/11/22
 **/
public abstract class BaseChatHandler {
    protected ChatType chatType;

    public BaseChatHandler(ChatType chatType) {
        this.chatType = chatType;
    }

    public abstract void handle(Role role, ChatReq chatReq);
}
