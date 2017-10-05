package me.kolek.fix.engine;

import me.kolek.fix.FixMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FixSessionListener extends Remote {
    void onLogon() throws RemoteException;

    void onLogout() throws RemoteException;

    void onReset() throws RemoteException;

    void onMessageSent(FixMessage message) throws RemoteException;

    void onMessageReceived(FixMessage message) throws RemoteException;

    void onException(Throwable cause) throws RemoteException;
}
