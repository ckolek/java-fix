package me.kolek.fix.engine.runtime;

import me.kolek.fix.FixMessage;
import me.kolek.fix.engine.FixSessionId;

public interface FixEngineListener {
    void onLogon(FixSessionId sessionId);

    void onLogout(FixSessionId sessionId);

    void onReset(FixSessionId sessionId);

    void onMessageSent(FixSessionId sessionId, FixMessage message);

    void onMessageReceived(FixSessionId sessionId, FixMessage message);

    void onException(FixSessionId sessionId, Throwable cause);
}
