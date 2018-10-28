package com.mmorpg.mbdl.framework.communicate.websocket.model;

import io.netty.channel.ChannelId;

public abstract class AbstractSession implements ISession {
    private ChannelId id;
    private String ip;
    /**
     * 创建session时处于连接状态
      */
    private SessionState state = SessionState.CONNECTED;

    public AbstractSession(ChannelId id, String ip) {
        this.id = id;
        this.ip = ip;
    }

    @Override
    public ChannelId getId() {
        return id;
    }

    @Override
    public String getIp() {
        return ip;
    }

    @Override
    public SessionState getState() {
        return state;
    }

    @Override
    public void setState(SessionState state) {
        this.state = state;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractSession another = (AbstractSession) obj;
        if (id == null) {
            if (another.id != null) {
                return false;
            }
        } else if (!id.equals(another.id)) {
            return false;
        }
        return true;
    }
}
