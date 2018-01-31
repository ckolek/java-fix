package me.kolek.fix.engine.quickfixj;

import me.kolek.fix.constants.BeginString;
import me.kolek.fix.engine.FixEngine;
import me.kolek.fix.engine.FixEngineCallback;
import me.kolek.fix.engine.FixSession;
import me.kolek.fix.engine.FixSessionId;
import me.kolek.util.function.ThrowingFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

class QfjFixEngine extends UnicastRemoteObject implements FixEngine {
    private static final Logger logger = LoggerFactory.getLogger(QfjFixEngine.class);

    private final FixEngineCallback callback;
    private final Initiator initiator;
    private final Acceptor acceptor;

    private final Map<SessionID, QfjFixSession> sessions;

    QfjFixEngine(Map<BeginString, DataDictionary> dictionaries, FixEngineCallback callback,
            ThrowingFunction<Application, Initiator, ConfigError> initiatorConstructor,
            ThrowingFunction<Application, Acceptor, ConfigError> acceptorConstructor)
            throws ConfigError, RemoteException {
        this.callback = callback;

        List<SessionID> sessionIds = new ArrayList<>();
        if ((this.initiator = initiatorConstructor.apply(application)) != null) {
            this.initiator.start();
            sessionIds.addAll(initiator.getSessions());
        }
        if ((this.acceptor = acceptorConstructor.apply(application)) != null) {
            this.acceptor.start();
            sessionIds.addAll(acceptor.getSessions());
        }

        Map<SessionID, QfjFixSession> sessions = new HashMap<>();
        for (SessionID sessionID : sessionIds) {
            QfjFixSession session = new QfjFixSession(sessionID, Session.lookupSession(sessionID), dictionaries);
            sessions.put(sessionID, session);
        }
        this.sessions = Collections.unmodifiableMap(sessions);
    }

    @Override
    public FixSession getSession(FixSessionId sessionId) throws RemoteException {
        return sessions.get(QfjUtil.toSessionID(sessionId));
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

    private final Application application = new Application() {
        @Override
        public void onCreate(SessionID sessionId) {
            try {
                callback.onSessionAvailable(QfjUtil.toFixSessionId(sessionId));
            } catch (RemoteException e) {
                logger.error("failed to call engine callback", e);
            }
        }

        @Override
        public void onLogon(SessionID sessionId) {
            sessions.get(sessionId).fireOnLogon();
        }

        @Override
        public void onLogout(SessionID sessionId) {
            sessions.get(sessionId).fireOnLogout();
        }

        @Override
        public void toAdmin(Message message, SessionID sessionId) {
            QfjMessage qfjMessage = (QfjMessage) message;
            sessions.get(sessionId).fireOnMessageSent(qfjMessage.getFixMessage());
        }

        @Override
        public void fromAdmin(Message message, SessionID sessionId)
                throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
            QfjMessage qfjMessage = (QfjMessage) message;
            sessions.get(sessionId).fireOnMessageReceived(qfjMessage.getFixMessage());
        }

        @Override
        public void toApp(Message message, SessionID sessionId) throws DoNotSend {
            QfjMessage qfjMessage = (QfjMessage) message;
            sessions.get(sessionId).fireOnMessageSent(qfjMessage.getFixMessage());
        }

        @Override
        public void fromApp(Message message, SessionID sessionId)
                throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
            QfjMessage qfjMessage = (QfjMessage) message;
            sessions.get(sessionId).fireOnMessageReceived(qfjMessage.getFixMessage());
        }
    };
}
