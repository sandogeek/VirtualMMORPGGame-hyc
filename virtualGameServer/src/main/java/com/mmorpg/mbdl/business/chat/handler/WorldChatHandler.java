package com.mmorpg.mbdl.business.chat.handler;

import com.mmorpg.mbdl.business.chat.model.ChatType;
import com.mmorpg.mbdl.business.chat.packet.ChatMessage;
import com.mmorpg.mbdl.business.chat.packet.ChatReq;
import com.mmorpg.mbdl.business.chat.packet.ChatResp;
import com.mmorpg.mbdl.business.role.manager.RoleManager;
import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.framework.thread.task.AbstractTask;
import com.mmorpg.mbdl.framework.thread.task.TaskDispatcher;

/**
 * 世界聊天处理器
 *
 * @author Sando Geek
 * @since v1.0 2019/11/22
 **/
public class WorldChatHandler extends BaseChatHandler {

    public WorldChatHandler(ChatType chatType) {
        super(chatType);
    }

    @Override
    public void handle(Role role, ChatReq chatReq) {
        /**
         * 每个聊天请求生成新的任务，根据频道ID拿到对应的队列，然后把任务分发到这个队列中即可保证所有玩家显示的消息的顺序一致
         */
        AbstractTask<ChatType, Long> task = new AbstractTask<ChatType, Long>(chatType) {

            @Override
            public String taskName() {
                return "聊天任务";
            }

            @Override
            public void execute() {
                ChatResp chatResp = new ChatResp();
                chatResp.setResult(true);
                ChatMessage chatMessage = new ChatMessage(role.getRoleId(), role.getName(), chatType, chatReq.getContent());
                RoleManager.getInstance().getSession2Role().values().stream().filter(role1 -> !role.equals(role1)).forEach(roleTemp -> {
                    roleTemp.sendPacket(chatMessage);
                });
            }
        }.setLogOrNot(false);

        // 把消息提交到任务队列串行发出，以保证所有玩家看到的消息顺序一致
        TaskDispatcher.getInstance().dispatch(task);
    }
}
