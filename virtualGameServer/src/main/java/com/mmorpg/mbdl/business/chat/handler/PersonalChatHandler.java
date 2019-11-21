package com.mmorpg.mbdl.business.chat.handler;

import com.mmorpg.mbdl.business.chat.model.ChatType;
import com.mmorpg.mbdl.business.chat.packet.ChatReq;
import com.mmorpg.mbdl.business.role.model.Role;

/**
 * 个人聊天处理器
 *
 * @author Sando Geek
 * @since v1.0 2019/11/22
 **/
public class PersonalChatHandler extends BaseChatHandler {

    public PersonalChatHandler(ChatType chatType) {
        super(chatType);
    }

    @Override
    public void handle(Role role, ChatReq chatReq) {

    }
}
