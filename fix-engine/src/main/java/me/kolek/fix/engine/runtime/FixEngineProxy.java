package me.kolek.fix.engine.runtime;

import me.kolek.fix.FixDictionary;
import me.kolek.fix.FixMessage;
import me.kolek.fix.engine.*;
import me.kolek.fix.engine.config.FixEngineConfiguration;
import me.kolek.util.concurrent.LockUtil;
import me.kolek.util.function.ThrowingConsumer;
import me.kolek.util.function.ThrowingFunction;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.*;

public class FixEngineProxy {
    private final String factoryAddress;
    private final FixEngineConfiguration configuration;
    private final FixDictionary dictionary;

    private final ReadWriteLock lock;
    private final Condition condition;

    private FixEngine engine;
    private final Map<FixSessionId, FixSessionProxy> sessions;

    private final Collection<FixEngineListener> listeners;

    private Thread connectionThread;

    public FixEngineProxy(String factoryAddress, FixEngineConfiguration configuration, FixDictionary dictionary) {
        this.factoryAddress = factoryAddress;
        this.configuration = configuration;
        this.dictionary = dictionary;

        this.lock = new ReentrantReadWriteLock(false);
        this.condition = lock.writeLock().newCondition();

        this.sessions = new TreeMap<>();

        this.listeners = new HashSet<>();
    }

    public synchronized void initialize() {
        if (connectionThread != null) {
            throw new IllegalStateException("FIX engine proxy has already been initialized");
        }
        connectionThread = new Thread(this::maintainConnection);
        connectionThread.start();
    }

    public synchronized void destroy() {
        if (connectionThread == null) {
            throw new IllegalStateException("FIX engine proxy has not been initialized");
        }
        connectionThread.interrupt();
        try {
            connectionThread.join();
        } catch (InterruptedException e) {
            // ignore
        }
        shutdown();
    }

    public void addListener(FixEngineListener listener) {
        listeners.add(listener);
    }

    public void removeListener(FixEngineListener listener) {
        listeners.remove(listener);
    }

    public Collection<FixSessionId> getSessionIds() {
        return sessions.keySet();
    }

    public void logon(FixSessionId sessionId) throws FixEngineException {
        useSession(sessionId, FixSessionProxy::logon);
    }

    public void logout(FixSessionId sessionId) throws FixEngineException {
        useSession(sessionId, FixSessionProxy::logout);
    }

    public int getIncomingMsgSeqNum(FixSessionId sessionId) throws FixEngineException {
        return useSession(sessionId, FixSessionProxy::getIncomingMsgSeqNum);
    }

    public void setIncomingMsgSeqNum(FixSessionId sessionId, int msgSeqNum) throws FixEngineException {
        useSession(sessionId, s -> { s.setIncomingMsgSeqNum(msgSeqNum); });
    }

    public int getOutgoingMsgSeqNum(FixSessionId sessionId) throws FixEngineException {
        return useSession(sessionId, FixSessionProxy::getOutgoingMsgSeqNum);
    }

    public void setOutgoingMsgSeqNum(FixSessionId sessionId, int msgSeqNum) throws FixEngineException {
        useSession(sessionId, s -> { s.setOutgoingMsgSeqNum(msgSeqNum); });
    }

    public void reset(FixSessionId sessionId) throws FixEngineException {
        useSession(sessionId, FixSessionProxy::reset);
    }

    public boolean send(FixSessionId sessionId, FixMessage message) throws FixEngineException {
        return useSession(sessionId, s -> { return s.send(message); });
    }

    private FixSessionProxy getSession(FixSessionId sessionId) throws FixEngineException {
        if (engine == null) {
            throw new FixEngineException("engine is not connected");
        }
        FixSessionProxy session = sessions.get(sessionId);
        if (session == null) {
            throw new FixEngineException("session with ID " + sessionId + " is not available");
        }
        return session;
    }

    private void useSession(FixSessionId sessionId, ThrowingConsumer<FixSessionProxy, RemoteException> action)
            throws FixEngineException {
        try {
            LockUtil.doLocked(lock.readLock(), () -> action.accept(getSession(sessionId)));
        } catch (RemoteException e) {
            throw handleException(e);
        }
    }

    private <T> T useSession(FixSessionId sessionId, ThrowingFunction<FixSessionProxy, T, RemoteException> action)
            throws FixEngineException {
        try {
            return LockUtil.doLocked(lock.readLock(), () -> action.apply(getSession(sessionId)));
        } catch (RemoteException e) {
            throw handleException(e);
        }
    }

    private FixEngineException handleException(RemoteException cause) {
        LockUtil.doLocked(lock.writeLock(), condition::signal);
        if (cause instanceof FixEngineException) {
            return (FixEngineException) cause;
        } else {
            return new FixEngineException(cause.getMessage());
        }
    }

    private void shutdown() {
        LockUtil.doLocked(lock.writeLock(), () -> {
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
        });
    }

    private void maintainConnection() {
        while (!Thread.interrupted()) {
            try {
                LockUtil.doLocked(lock.writeLock(), () -> {
                    try {
                        if (engine != null) {
                            condition.await();
                        }
                        shutdown();
                        FixEngineFactory factory = (FixEngineFactory) Naming.lookup(factoryAddress);
                        engine = factory.launchEngine(configuration, dictionary, new FixEngineProxyCallback());
                    } catch (NotBoundException | MalformedURLException | RemoteException e) {
                        e.printStackTrace();
                        shutdown();
                    }
                });
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private class FixEngineProxyCallback extends UnicastFixEngineCallback {
        private FixEngineProxyCallback() throws RemoteException {}

        @Override
        public void onSessionAvailable(FixSessionId sessionId) throws RemoteException {
            LockUtil.doLocked(lock.writeLock(), () -> {
                FixSession session = engine.getSession(sessionId);
                sessions.put(sessionId, new FixSessionProxy(sessionId, session));
            });
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

        private void logon() throws RemoteException {
            session.logon();
        }

        private void logout() throws RemoteException {
            session.logout();
        }

        private void reset() throws RemoteException {
            session.reset();
        }

        private int getIncomingMsgSeqNum() throws RemoteException {
            return session.getIncomingMsgSeqNum();
        }

        private void setIncomingMsgSeqNum(int msgSeqNum) throws RemoteException {
            session.setIncomingMsgSeqNum(msgSeqNum);
        }

        private int getOutgoingMsgSeqNum() throws RemoteException {
            return session.getOutgoingMsgSeqNum();
        }

        private void setOutgoingMsgSeqNum(int msgSeqNum) throws RemoteException {
            session.setOutgoingMsgSeqNum(msgSeqNum);
        }

        private boolean send(FixMessage message) throws RemoteException {
            return session.send(message);
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
