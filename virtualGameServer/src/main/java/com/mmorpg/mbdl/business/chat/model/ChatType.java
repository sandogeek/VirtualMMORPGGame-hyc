package com.mmorpg.mbdl.business.chat.model;

import com.baidu.bjf.remoting.protobuf.EnumReadable;
import com.mmorpg.mbdl.business.chat.handler.BaseChatHandler;
import com.mmorpg.mbdl.business.chat.handler.PersonalChatHandler;
import com.mmorpg.mbdl.business.chat.handler.WorldChatHandler;
import com.mmorpg.mbdl.framework.thread.interfaces.Dispatchable;

/**
 * 聊天类型
 *
 * @author Sando Geek
 * @since v1.0 2019/11/21
 **/
public enum ChatType implements Dispatchable<Long>, EnumReadable {
    /**
     * 世界聊天
     */
    WORLD(1) {
        @Override
        public BaseChatHandler getBaseChatHandler() {
            return new WorldChatHandler(this);
        }
    },
    /**
     * 个人聊天
     */
    PERSONAL(2) {
        @Override
        public BaseChatHandler getBaseChatHandler() {
            return new PersonalChatHandler(this);
        }
    };
    private int code;

    ChatType(int code) {
        this.code = code;
    }

    public abstract BaseChatHandler getBaseChatHandler();
    @Override
    public int value() {
        return code;
    }


    @Override
    public Long dispatchId() {
        return (long) code;
    }
}
