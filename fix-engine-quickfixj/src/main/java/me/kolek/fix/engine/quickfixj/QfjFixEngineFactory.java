package me.kolek.fix.engine.quickfixj;

import me.kolek.fix.engine.*;
import me.kolek.fix.engine.config.FixEngineConfiguration;
import me.kolek.fix.engine.config.FixSessionConfiguration;
import me.kolek.util.tuple.Tuple;
import me.kolek.util.tuple.Tuple3;
import quickfix.*;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

class QfjFixEngineFactory extends UnicastRemoteObject implements FixEngineFactory {
    private File messageStoreDir;
    private File logDir;

    private QfjFixEngine engine;
    private FixEngineConfiguration configuration;

    QfjFixEngineFactory(int port) throws RemoteException {
        super(port);
    }

    void setMessageStoreDir(File messageStoreDir) throws IOException {
        if (!(this.messageStoreDir = messageStoreDir).exists() && !this.messageStoreDir.mkdirs()) {
            throw new IOException("failed to create message store directory: " + messageStoreDir);
        }
    }

    void setLogDir(File logDir) throws IOException {
        if (!(this.logDir = logDir).exists() && !this.logDir.mkdirs()) {
            throw new IOException("failed to create log directory: " + logDir);
        }
    }

    @Override
    public FixEngine launchEngine(FixEngineConfiguration configuration, FixDictionaryProvider dictionaryProvider,
            FixEngineCallback callback) throws RemoteException {
        if (engine != null) {
            if (this.configuration != null && this.configuration.equals(configuration)) {
                return engine;
            }
            try {
                engine.shutdown();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        try {
            Tuple3<SessionSettings, Boolean, Boolean> result = toSessionSettings(configuration);
            SessionSettings settings = result.first();
            MessageStoreFactory messageStoreFactory = getMessageStoreFactory(settings);
            LogFactory logFactory = getLogFactory(settings);
            MessageFactory messageFactory = getMessageFactory(settings);
            this.engine = new QfjFixEngine(dictionaryProvider, callback,
                    app -> result.second() ? new SocketInitiator(app, messageStoreFactory, settings, logFactory,
                            messageFactory) : null,
                    app -> result.third() ? new SocketAcceptor(app, messageStoreFactory, settings, logFactory,
                            messageFactory) : null);
            this.configuration = configuration;
            return this.engine;
        } catch (quickfix.RuntimeError | ConfigError e) {
            throw new FixEngineException("failed to create engine", e);
        }
    }

    private Tuple3<SessionSettings, Boolean, Boolean> toSessionSettings(FixEngineConfiguration configuration)
            throws ConfigError {
        SessionSettings settings = new SessionSettings();
        if (configuration.getAcceptorHost() != null) {
            settings.setString(Acceptor.SETTING_SOCKET_ACCEPT_ADDRESS, configuration.getAcceptorHost());
        }
        if (configuration.getAcceptorPort() != null) {
            settings.setLong(Acceptor.SETTING_SOCKET_ACCEPT_PORT, configuration.getAcceptorPort());
        }

        boolean hasInitiator = false, hasAcceptor = false;
        for (FixSessionConfiguration sessionConfiguration : configuration.getSessions()) {
            Dictionary dictionary = QfjUtil.toSessionSettings(sessionConfiguration);
            settings.set(QfjUtil.toSessionID(sessionConfiguration.getSessionId()), dictionary);
            if (sessionConfiguration.isAcceptor()) {
                hasAcceptor = true;
            } else {
                hasInitiator = true;
            }
        }
        return Tuple.of(settings, hasInitiator, hasAcceptor);
    }

    private MessageStoreFactory getMessageStoreFactory(SessionSettings settings) {
        if (messageStoreDir != null) {
            settings.setString(FileStoreFactory.SETTING_FILE_STORE_PATH, messageStoreDir.getAbsolutePath());
        }

        return new FileStoreFactory(settings);
    }

    private LogFactory getLogFactory(SessionSettings settings) {
        if (logDir != null) {
            settings.setString(FileLogFactory.SETTING_FILE_LOG_PATH, logDir.getAbsolutePath());
        }

        return new FileLogFactory(settings);
    }

    private MessageFactory getMessageFactory(SessionSettings settings) {
        return new DefaultMessageFactory();
    }
}
