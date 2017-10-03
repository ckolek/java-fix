package me.kolek.fix.engine;

import java.rmi.RemoteException;

public class FixEngineException extends RemoteException {
    public FixEngineException() {
    }

    public FixEngineException(String s) {
        super(s);
    }

    public FixEngineException(String s, Throwable cause) {
        super(s, cause);
    }
}
