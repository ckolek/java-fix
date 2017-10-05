package me.kolek.fix.engine.runtime;

import me.kolek.fix.engine.FixEngineCallback;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public abstract class UnicastFixEngineCallback extends UnicastRemoteObject implements FixEngineCallback {
    protected UnicastFixEngineCallback() throws RemoteException {
    }

    protected UnicastFixEngineCallback(int port) throws RemoteException {
        super(port);
    }

    protected UnicastFixEngineCallback(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf)
            throws RemoteException {
        super(port, csf, ssf);
    }
}
