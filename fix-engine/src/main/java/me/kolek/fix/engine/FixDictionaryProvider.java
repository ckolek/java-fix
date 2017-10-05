package me.kolek.fix.engine;

import me.kolek.fix.FixDictionary;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FixDictionaryProvider extends Remote {
    FixDictionary getDictionary(FixSessionId sessionId, String applVerId) throws RemoteException;
}
