package com.mmorpg.mbdl.framework.event.preset;

import io.netty.channel.ChannelId;

public class SessionCloseEvent {
    private ChannelId channelId;

    public SessionCloseEvent(ChannelId channelId) {
        this.channelId = channelId;
    }

    public ChannelId getChannelId() {
        return channelId;
    }
}
