package me.kolek.fix.engine.runtime;

import me.kolek.fix.FixMessage;
import me.kolek.fix.engine.*;
import me.kolek.fix.engine.config.FixEngineConfiguration;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FixEngineProxy {
    private final String factoryAddress;
    private final FixEngineConfiguration configuration;
    private final FixDictionaryProvider dictionaryProvider;

    private final Lock lock;
    private final Condition condition;

    private FixEngine engine;
    private final Map<FixSessionId, FixSessionProxy> sessions;

    private final Collection<FixEngineListener> listeners;

    private Thread connectionThread;

    public FixEngineProxy(String factoryAddress, FixEngineConfiguration configuration,
            FixDictionaryProvider dictionaryProvider) {
        this.factoryAddress = factoryAddress;
        this.configuration = configuration;
        this.dictionaryProvider = dictionaryProvider;

        this.lock = new ReentrantLock(true);
        this.condition = lock.newCondition();

        this.sessions = new HashMap<>();

        this.listeners = new HashSet<>();
    }

    public void initialize() {
        connectionThread = new Thread(this::maintainConnection);
        connectionThread.start();
    }

    public void destroy() {
        if (connectionThread != null) {
            connectionThread.interrupt();
            try {
                connectionThread.join();
            } catch (InterruptedException e) {
                // ignore
            }
        }
        shutdown();
    }

    public void addListener(FixEngineListener listener) {
        listeners.add(listener);
    }

    public void removeListener(FixEngineListener listener) {
        listeners.remove(listener);
    }

    public String getBeginString(String sessionId) throws FixEngineException {
        return null;
    }

    public String getTargetCompId(String sessionId) throws FixEngineException {
        return null;
    }

    public String getTargetSubId(String sessionId) throws FixEngineException {
        return null;
    }

    public String getTargetLocationId(String sessionId) throws FixEngineException {
        return null;
    }

    public String getSenderCompId(String sessionId) throws FixEngineException {
        return null;
    }

    public String getSenderSubId(String sessionId) throws FixEngineException {
        return null;
    }

    public String getSenderLocationId(String sessionId) throws FixEngineException {
        return null;
    }

    void logon(String sessionId) throws FixEngineException {

    }

    void logout(String sessionId) throws FixEngineException {

    }

    int getIncomingMsgSeqNum(String sessionId) throws FixEngineException {
        return 0;
    }

    void setIncomingMsgSeqNum(String sessionId, int msgSeqNum) throws FixEngineException {

    }

    int getOutgoingMsgSeqNum(String sessionId) throws FixEngineException {
        return 0;
    }

    void setOutgoingMsgSeqNum(String sessionId, int msgSeqNum) throws FixEngineException {

    }

    void reset(String sessionId) throws FixEngineException {

    }

    boolean send(String sessionId, FixMessage message) throws FixEngineException {
        return false;
    }

    private void shutdown() {
        for (FixSessionProxy session : sessions.values()) {
            try {
                session.destroy();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        sessions.clear();
        try {
            engine.shutdown();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        engine = null;
    }

    private void maintainConnection() {
        while (!Thread.interrupted()) {
            try {
                try {
                    lock.lock();
                    if (engine != null) {
                        condition.await();
                    }
                    shutdown();
                    FixEngineFactory factory = (FixEngineFactory) Naming.lookup(factoryAddress);
                    engine = factory.launchEngine(configuration, dictionaryProvider, new FixEngineProxyCallback());
                } catch (NotBoundException | MalformedURLException | RemoteException e) {
                    e.printStackTrace();
                    shutdown();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private class FixEngineProxyCallback extends UnicastFixEngineCallback {
        private FixEngineProxyCallback() throws RemoteException {}

        @Override
        public void onSessionAvailable(FixSessionId sessionId) throws RemoteException {
            FixSession session = engine.getSession(sessionId);
            sessions.put(sessionId, new FixSessionProxy(sessionId, session));
        }
    }

    private class FixSessionProxy extends UnicastRemoteObject implements FixSessionListener {
        private final FixSessionId sessionId;
        private final FixSession session;

        private FixSessionProxy(FixSessionId sessionId, FixSession session) throws RemoteException {
            this.sessionId = sessionId;
            this.session = session;
            this.session.registerListener(this);
        }

        private void destroy() throws RemoteException {
            try {
                session.unregisterListener();
            } finally {
                UnicastRemoteObject.unexportObject(this, true);
            }
        }

        @Override
        public void onLogon() throws RemoteException {
            listeners.forEach(listener -> listener.onLogon(sessionId));
        }

        @Override
        public void onLogout() throws RemoteException {
            listeners.forEach(listener -> listener.onLogout(sessionId));
        }

        @Override
        public void onReset() throws RemoteException {
            listeners.forEach(listener -> listener.onReset(sessionId));
        }

        @Override
        public void onMessageSent(FixMessage message) throws RemoteException {
            listeners.forEach(listener -> listener.onMessageSent(sessionId, message));
        }

        @Override
        public void onMessageReceived(FixMessage message) throws RemoteException {
            listeners.forEach(listener -> listener.onMessageReceived(sessionId, message));
        }

        @Override
        public void onException(Throwable cause) throws RemoteException {
            listeners.forEach(listener -> listener.onException(sessionId, cause));
        }
    }
}
