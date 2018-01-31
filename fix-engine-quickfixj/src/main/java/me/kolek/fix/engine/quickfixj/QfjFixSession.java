package me.kolek.fix.engine.quickfixj;

import me.kolek.fix.FixMessage;
import me.kolek.fix.constants.BeginString;
import me.kolek.fix.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.DataDictionary;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.field.ApplVerID;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

class QfjFixSession extends UnicastRemoteObject implements FixSession {
    private static final Logger logger = LoggerFactory.getLogger(QfjFixSession.class);

    private final FixSessionId sessionId;
    private final Session session;

    private final Map<BeginString, DataDictionary> dictionaries;

    private FixSessionListener listener;

    QfjFixSession(SessionID sessionId, Session session, Map<BeginString, DataDictionary> dictionaries)
            throws RemoteException {
        this.sessionId = QfjUtil.toFixSessionId(sessionId);
        this.session = session;
        this.dictionaries = dictionaries;
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
    public boolean isLoggedOn() throws RemoteException {
        return session.isLoggedOn();
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
        try {
            ApplVerID defApplVerID = session.getTargetDefaultApplicationVersionID();
            return session.send(QfjUtil
                    .toMessage(dictionaries, message, defApplVerID != null ? defApplVerID.getValue() : null));
        } catch (InvalidMessageException e) {
            throw new FixEngineException("invalid message: " + e.getMessage());
        }
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
