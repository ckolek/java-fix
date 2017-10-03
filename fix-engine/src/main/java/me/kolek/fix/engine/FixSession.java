package me.kolek.fix.engine;

import me.kolek.fix.FixMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FixSession extends Remote {
    String getSessionId() throws RemoteException;

    String getBeginString() throws RemoteException;

    String getTargetCompId() throws RemoteException;

    String getTargetSubId() throws RemoteException;

    String getTargetLocationId() throws RemoteException;

    String getSenderCompId() throws RemoteException;

    String getSenderSubId() throws RemoteException;

    String getSenderLocationId() throws RemoteException;

    void logon() throws RemoteException;

    void logout() throws RemoteException;

    int getIncomingMsgSeqNum() throws RemoteException;

    void setIncomingMsgSeqNum(int msgSeqNum) throws RemoteException;

    int getOutgoingMsgSeqNum() throws RemoteException;

    void setOutgoingMsgSeqNum(int msgSeqNum) throws RemoteException;

    void reset() throws RemoteException;

    boolean send(FixMessage message) throws RemoteException;
}
