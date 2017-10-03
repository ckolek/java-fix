package me.kolek.fix.engine.quickfixj;

import me.kolek.fix.FixMessage;
import me.kolek.fix.engine.FixEngineException;
import me.kolek.fix.engine.FixSession;
import quickfix.InvalidMessage;
import quickfix.Session;
import quickfix.SessionID;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

class QfjFixSession extends UnicastRemoteObject implements FixSession {
    private final SessionID sessionId;
    private final Session session;

    QfjFixSession(SessionID sessionId, Session session) throws RemoteException {
        this.sessionId = sessionId;
        this.session = session;
    }

    @Override
    public String getSessionId() {
        return sessionId.getSessionQualifier();
    }

    @Override
    public String getBeginString() throws RemoteException {
        return sessionId.getBeginString();
    }

    @Override
    public String getTargetCompId() throws RemoteException {
        return sessionId.getTargetCompID();
    }

    @Override
    public String getTargetSubId() throws RemoteException {
        return sessionId.getTargetSubID();
    }

    @Override
    public String getTargetLocationId() throws RemoteException {
        return sessionId.getTargetLocationID();
    }

    @Override
    public String getSenderCompId() throws RemoteException {
        return sessionId.getSenderCompID();
    }

    @Override
    public String getSenderSubId() throws RemoteException {
        return sessionId.getSenderSubID();
    }

    @Override
    public String getSenderLocationId() throws RemoteException {
        return sessionId.getSenderLocationID();
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
        try {
            return session.send(new QfjMessage(message));
        } catch (InvalidMessage e) {
            throw new FixEngineException("invalid message: " + e.getMessage());
        }
    }
}
