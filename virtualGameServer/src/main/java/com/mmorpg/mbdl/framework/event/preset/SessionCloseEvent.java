package com.mmorpg.mbdl.framework.event.preset;

import com.mmorpg.mbdl.framework.communicate.websocket.model.ISession;

public class SessionCloseEvent {
    private ISession session;

    public SessionCloseEvent(ISession session) {
        this.session = session;
    }

    public ISession getSession() {
        return session;
    }
}
