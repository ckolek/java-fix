package me.kolek.fix.engine.quickfixj;

import me.kolek.fix.FixDictionary;
import me.kolek.fix.FixMessage;
import me.kolek.fix.constants.TagNum;
import me.kolek.fix.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.Session;
import quickfix.SessionID;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

class QfjFixSession extends UnicastRemoteObject implements FixSession {
    private static final Logger logger = LoggerFactory.getLogger(QfjFixSession.class);

    private final FixSessionId sessionId;
    private final Session session;

    private final FixDictionaryProvider dictionaryProvider;

    private FixSessionListener listener;

    QfjFixSession(SessionID sessionId, Session session, FixDictionaryProvider dictionaryProvider) throws RemoteException {
        this.sessionId = QfjUtil.toFixSessionId(sessionId);
        this.session = session;
        this.dictionaryProvider = dictionaryProvider;
    }

    @Override
    public FixSessionId getSessionId() {
        return sessionId;
    }

    @Override
    public void registerListener(FixSessionListener listener) throws RemoteException {
        this.listener = listener;
    }

    @Override
    public void unregisterListener() throws RemoteException {
        this.listener = null;
    }

    @Override
    public void logon() throws RemoteException {
        session.logon();
    }

    @Override
    public void logout() throws RemoteException {
        session.logout();
    }

    @Override
    public int getIncomingMsgSeqNum() throws RemoteException {
        return session.getExpectedTargetNum();
    }

    @Override
    public void setIncomingMsgSeqNum(int msgSeqNum) throws RemoteException {
        try {
            session.setNextTargetMsgSeqNum(msgSeqNum);
        } catch (IOException e) {
            throw new FixEngineException("failed to set incoming MsgSeqNum", e);
        }
    }

    @Override
    public int getOutgoingMsgSeqNum() throws RemoteException {
        return session.getExpectedSenderNum();
    }

    @Override
    public void setOutgoingMsgSeqNum(int msgSeqNum) throws RemoteException {
        try {
            session.setNextSenderMsgSeqNum(msgSeqNum);
        } catch (IOException e) {
            throw new FixEngineException("failed to set outgoing MsgSeqNum", e);
        }
    }

    @Override
    public void reset() throws RemoteException {
        try {
            session.reset();
        } catch (IOException e) {
            throw new FixEngineException("failed to reset session", e);
        }
    }

    @Override
    public boolean send(FixMessage message) throws RemoteException {
        String beginString = message.getBeginString()
                .orElseThrow(() -> new FixEngineException("message must have a BeginString value"));
        String applVerId;
        if (beginString.startsWith("FIXT")) {
            applVerId = message.getValue(TagNum.ApplVerID).orElse(null);
        } else {
            applVerId = null;
        }

        FixDictionary dictionary = dictionaryProvider.getDictionary(sessionId, applVerId);

        return session.send(QfjUtil.toMessage(dictionary, message));
    }

    void fireOnLogon() {
        if (listener != null) {
            try {
                listener.onLogon();
            } catch (RemoteException e) {
                logger.error("failed to notify listener of logon", e);
            }
        }
    }

    void fireOnLogout() {
        if (listener != null) {
            try {
                listener.onLogout();
            } catch (RemoteException e) {
                logger.error("failed to notify listener of logout", e);
            }
        }
    }

    void fireOnReset() {
        if (listener != null) {
            try {
                listener.onReset();
            } catch (RemoteException e) {
                logger.error("failed to notify listener of reset", e);
            }
        }
    }

    void fireOnMessageSent(FixMessage message) {
        if (listener != null) {
            try {
                listener.onMessageSent(message);
            } catch (RemoteException e) {
                logger.error("failed to notify listener of message sent", e);
            }
        }
    }

    void fireOnMessageReceived(FixMessage message) {
        if (listener != null) {
            try {
                listener.onMessageReceived(message);
            } catch (RemoteException e) {
                logger.error("failed to notify listener of message received", e);
            }
        }
    }

    void fireOnException(Throwable cause) {
        if (listener != null) {
            try {
                listener.onException(cause);
            } catch (RemoteException e) {
                logger.error("failed to notify listener of exception", e);
            }
        }
    }
}
