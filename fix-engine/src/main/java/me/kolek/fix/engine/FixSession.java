package me.kolek.fix.engine;

import me.kolek.fix.FixMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FixSession extends Remote {
    FixSessionId getSessionId() throws RemoteException;

    void registerListener(FixSessionListener listener) throws RemoteException;

    void unregisterListener() throws RemoteException;

    void logon() throws RemoteException;

    void logout() throws RemoteException;

    int getIncomingMsgSeqNum() throws RemoteException;

    void setIncomingMsgSeqNum(int msgSeqNum) throws RemoteException;

    int getOutgoingMsgSeqNum() throws RemoteException;

    void setOutgoingMsgSeqNum(int msgSeqNum) throws RemoteException;

    void reset() throws RemoteException;

    boolean send(FixMessage message) throws RemoteException;
}
