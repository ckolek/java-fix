package me.kolek.fix.engine;

import me.kolek.fix.engine.config.FixEngineConfiguration;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FixEngineFactory extends Remote {
    FixEngine launchEngine(FixEngineConfiguration configuration) throws RemoteException;
}
