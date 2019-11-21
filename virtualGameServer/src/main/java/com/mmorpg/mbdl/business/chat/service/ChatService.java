package com.mmorpg.mbdl.business.chat.service;

import com.mmorpg.mbdl.business.chat.model.ChatType;
import com.mmorpg.mbdl.business.chat.packet.ChatReq;
import com.mmorpg.mbdl.business.role.model.Role;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 聊天服务
 *
 * @author Sando Geek
 * @since v1.0 2019/1/7
 **/
@Component
public class ChatService {
    private static ChatService self;

    @PostConstruct
    private void init() {
        self = this;
    }

    public static ChatService getInstance() {
        return self;
    }

    /**
     * 世界聊天、频道聊天、组队聊天，应该各有一个队列
     * 那么不同的请求应当生成不同的任务然后放到相应的队列里面去。
     * @param role
     * @param chatReq
     * @return
     */
    public void handleChatReq(Role role, ChatReq chatReq){
        ChatType chatType = chatReq.getChatType();
        chatType.getBaseChatHandler().handle(role, chatReq);
    }
}
