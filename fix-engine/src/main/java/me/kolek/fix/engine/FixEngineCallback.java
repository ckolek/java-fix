package me.kolek.fix.engine;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FixEngineCallback extends Remote {
    void onSessionAvailable(FixSessionId sessionId) throws RemoteException;
}
