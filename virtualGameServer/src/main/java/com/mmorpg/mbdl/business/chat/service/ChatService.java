package com.mmorpg.mbdl.business.chat.service;

import com.mmorpg.mbdl.business.chat.packet.ChatMessage;
import com.mmorpg.mbdl.business.chat.packet.ChatReq;
import com.mmorpg.mbdl.business.chat.packet.ChatResp;
import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.business.role.manager.RoleManager;
import com.mmorpg.mbdl.framework.communicate.websocket.model.ISession;
import com.mmorpg.mbdl.framework.thread.task.AbstractTask;
import com.mmorpg.mbdl.framework.thread.task.BaseNormalTask;
import com.mmorpg.mbdl.framework.thread.task.TaskDispatcher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;

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
     * @param session
     * @param chatReq
     * @return
     */
    public void handleChatReq(ISession session, ChatReq chatReq){
        long targetId = chatReq.getTargetId();
        Long dispatcherId;
        AbstractTask<Serializable> task = null;
        if (targetId == 0L) {
            // 世界聊天
            dispatcherId = 0L;
            /**
             * 每个聊天请求生成新的任务，根据频道ID拿到对应的队列，然后把任务分发到这个队列中即可保证所有玩家显示的消息的顺序一致
             */
            task = new BaseNormalTask(dispatcherId) {

                @Override
                public String taskName() {
                    return "世界聊天任务";
                }

                @Override
                public void execute() {
                    ChatResp chatResp = new ChatResp();
                    chatResp.setResult(true);
                    RoleManager.getInstance().getRoleBySession(session).sendPacket(chatResp);
                    Role role = RoleManager.getInstance().getRoleBySession(session);
                    ChatMessage chatMessage = new ChatMessage(session.getRoleId(), role.getName(), 0, chatReq.getContent());
                    RoleManager.getInstance().getSession2Role().values().stream().filter(role1 -> !role.equals(role1)).forEach(roleTemp -> {
                        roleTemp.sendPacket(chatMessage);
                    });
                }
            }.setLogOrNot(false);
        } else {
            dispatcherId = 1L;
        }

        // 把消息提交到任务队列串行发出，以保证所有玩家看到的消息顺序一致
        TaskDispatcher.getInstance().dispatch(task);
    }
}
