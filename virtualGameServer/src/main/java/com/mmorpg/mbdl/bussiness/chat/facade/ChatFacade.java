package com.mmorpg.mbdl.bussiness.chat.facade;

import com.mmorpg.mbdl.bussiness.chat.packet.ChatReq;
import com.mmorpg.mbdl.bussiness.chat.packet.ChatResp;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketHandler;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketMethod;
import com.mmorpg.mbdl.framework.communicate.websocket.model.ISession;
import com.mmorpg.mbdl.framework.thread.task.Task;
import com.mmorpg.mbdl.framework.thread.task.TaskDispatcher;

@PacketHandler
public class ChatFacade {
    /**
     * 聊天请求与玩家其它请求并行(原则：需要保证顺序的队列化(串行)，要排除受到其它任务影响又不需要保证顺序的并行化)
     * 世界聊天、频道聊天、组队聊天，应该各有一个队列
     * 那么不同的请求应当生成不同的任务然后放到相应的队列里面去。
     * @param session
     * @param chatReq
     * @return
     */
    @PacketMethod(executeParallel = true)
    public void handleChatReq(ISession session, ChatReq chatReq){
        /**
         * 每个聊天请求生成新的任务，根据频道ID拿到对应的队列，然后把任务submit到这个队列中即可保证所有玩家显示的消息的顺序一致
         */
        Task task = new Task(chatReq.getChatChannelId()) {

            @Override
            public String taskName() {
                return String.format("聊天任务[频道id=%s]",chatReq.getChatChannelId());
            }

            @Override
            public void execute() {
                // 根据不同的频道，获取该频道所有的人的Set，通过循环这个Set,把消息发出去
                // for (Player player:
                //      ) {
                //    player.sendPacket(chatResp);
                // }
                ChatResp chatResp = new ChatResp();
                chatResp.setContent(chatReq.getContent());
                session.sendPacket(chatResp);
            }
        };
        // 把消息提交到任务队列串行发出，以保证所有玩家看到的消息顺序一致
        TaskDispatcher.getIntance().dispatch(task);
    }
}
