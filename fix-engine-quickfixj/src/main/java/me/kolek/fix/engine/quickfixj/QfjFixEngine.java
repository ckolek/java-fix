package me.kolek.fix.engine.quickfixj;

import me.kolek.fix.engine.FixEngine;
import me.kolek.fix.engine.FixSession;
import me.kolek.util.function.ThrowingFunction;
import quickfix.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

class QfjFixEngine extends UnicastRemoteObject implements FixEngine {
    private final Initiator initiator;
    private final Acceptor acceptor;

    private final Map<String, QfjFixSession> sessions;
    private final Application application = new Application() {
        @Override
        public void onCreate(SessionID sessionId) {

        }

        @Override
        public void onLogon(SessionID sessionId) {

        }

        @Override
        public void onLogout(SessionID sessionId) {

        }

        @Override
        public void toAdmin(Message message, SessionID sessionId) {

        }

        @Override
        public void fromAdmin(Message message, SessionID sessionId)
                throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {

        }

        @Override
        public void toApp(Message message, SessionID sessionId) throws DoNotSend {

        }

        @Override
        public void fromApp(Message message, SessionID sessionId)
                throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {

        }
    };

    QfjFixEngine(ThrowingFunction<Application, Initiator, ConfigError> initiatorConstructor,
            ThrowingFunction<Application, Acceptor, ConfigError> acceptorConstructor)
            throws ConfigError, RemoteException {
        List<SessionID> sessionIds = new ArrayList<>();
        if ((this.initiator = initiatorConstructor.apply(application)) != null) {
            this.initiator.start();
            sessionIds.addAll(initiator.getSessions());
        }
        if ((this.acceptor = acceptorConstructor.apply(application)) != null) {
            this.acceptor.start();
            sessionIds.addAll(acceptor.getSessions());
        }

        Map<String, QfjFixSession> sessions = new HashMap<>();
        for (SessionID sessionID : sessionIds) {
            QfjFixSession session = new QfjFixSession(sessionID, Session.lookupSession(sessionID));
            sessions.put(sessionID.getSessionQualifier(), session);
        }
        this.sessions = Collections.unmodifiableMap(sessions);
    }

    @Override
    public FixSession getSession(String sessionId) throws RemoteException {
        return sessions.get(sessionId);
    }

    @Override
    public void shutdown() throws RemoteException {
        if (initiator != null) {
            initiator.stop();
        }
        if (acceptor != null) {
            acceptor.stop();
        }
        try {
            for (FixSession session : sessions.values()) {
                UnicastRemoteObject.unexportObject(session, false);
            }
        } finally {
            UnicastRemoteObject.unexportObject(this, false);
        }
    }
}
