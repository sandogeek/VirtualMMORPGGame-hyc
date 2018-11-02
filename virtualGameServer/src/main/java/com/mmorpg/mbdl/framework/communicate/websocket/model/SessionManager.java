package com.mmorpg.mbdl.framework.communicate.websocket.model;

import io.netty.channel.ChannelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 会话管理器
 * @author sando
 */
@Component
public class SessionManager {
    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);

    private ConcurrentHashMap<ChannelId,ISession> channelId2WsSessions = new ConcurrentHashMap<>();

    /**
     * 添加ISession
     */
    public void add(ISession session){
        if (channelId2WsSessions.containsKey(session.getId())){
            logger.error("session[channelId={},IP={}]重复注册",session.getId(),session.getIp());
        } else {
            channelId2WsSessions.put(session.getId(),session);
        }
    }

    /**
     * 根据channelId移除相应的ISession
     * @param channelId
     */
    public void remove(ChannelId channelId){
        ISession session = channelId2WsSessions.get(channelId);
        session.close();
    }

    /**
     * 根据channelId获取session
     * @param channelId
     * @return 对应的ISession
     */
    public ISession getSession(ChannelId channelId){
        return channelId2WsSessions.get(channelId);
    }
}
