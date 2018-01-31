package me.kolek.fix.engine.config;

import me.kolek.fix.engine.FixSessionId;
import me.kolek.util.ObjectUtil;
import me.kolek.util.tuple.Tuple;
import me.kolek.util.tuple.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class FixSessionConfiguration implements Serializable {
    private final boolean acceptor;
    private final FixSessionId sessionId;
    private final String defaultApplVerId;
    private final String defaultApplExtId;
    private final String defaultCstmApplVerId;
    private final String username;
    private final String password;
    private final String newPassword;
    private final String passwordEncryption;
    private final boolean test;
    private final List<Tuple2<String, Integer>> addresses;
    private final Integer heartbeatInterval;

    public FixSessionConfiguration(boolean acceptor, FixSessionId sessionId, String defaultApplVerId,
            String defaultApplExtId, String defaultCstmApplVerId, String username, String password, String newPassword,
            String passwordEncryption, boolean test, List<Tuple2<String, Integer>> addresses, Integer heartbeatInterval) {
        this.acceptor = acceptor;
        this.sessionId = sessionId;
        this.defaultApplVerId = defaultApplVerId;
        this.defaultApplExtId = defaultApplExtId;
        this.defaultCstmApplVerId = defaultCstmApplVerId;
        this.username = username;
        this.password = password;
        this.newPassword = newPassword;
        this.passwordEncryption = passwordEncryption;
        this.test = test;
        this.addresses = Collections.unmodifiableList(addresses);
        this.heartbeatInterval = heartbeatInterval;
    }

    public static FixSessionConfiguration build(Consumer<FixSessionConfiguration.Builder> builderConsumer) {
        FixSessionConfiguration.Builder builder = new FixSessionConfiguration.Builder();
        builderConsumer.accept(builder);
        return builder.build();
    }

    public boolean isAcceptor() {
        return acceptor;
    }

    public FixSessionId getSessionId() {
        return sessionId;
    }

    public String getDefaultApplVerId() {
        return defaultApplVerId;
    }

    public String getDefaultApplExtId() {
        return defaultApplExtId;
    }

    public String getDefaultCstmApplVerId() {
        return defaultCstmApplVerId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getPasswordEncryption() {
        return passwordEncryption;
    }

    public boolean isTest() {
        return test;
    }

    public List<Tuple2<String, Integer>> getAddresses() {
        return addresses;
    }

    public Integer getHeartbeatInterval() {
        return heartbeatInterval;
    }

    @Override
    public int hashCode() {
        return Objects.hash(acceptor, sessionId, defaultApplVerId, addresses, heartbeatInterval);
    }

    @Override
    public boolean equals(Object obj) {
        return ObjectUtil.equals(this, obj, FixSessionConfiguration::isAcceptor, FixSessionConfiguration::getSessionId,
                FixSessionConfiguration::getDefaultApplVerId, FixSessionConfiguration::getAddresses,
                FixSessionConfiguration::getHeartbeatInterval);
    }

    public static class Builder {
        private boolean acceptor;
        private FixSessionId sessionId;
        private String defaultApplVerId;
        private String defaultApplExtId;
        private String defaultCstmApplVerId;
        private String username;
        private String password;
        private String newPassword;
        private String passwordEncryption;
        private boolean test;
        private final List<Tuple2<String, Integer>> addresses = new ArrayList<>();
        private Integer heartbeatInterval;

        public Builder acceptor() {
            this.acceptor = true;
            return this;
        }

        public Builder initiator() {
            this.acceptor = false;
            return this;
        }

        public Builder sessionId(FixSessionId sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public Builder sessionId(Consumer<FixSessionId.Builder> builderConsumer) {
            return sessionId(FixSessionId.build(builderConsumer));
        }

        public Builder defaultApplVerId(String defaultApplVerId) {
            this.defaultApplVerId = defaultApplVerId;
            return this;
        }

        public Builder defaultApplExtId(String defaultApplExtId) {
            this.defaultApplExtId = defaultApplExtId;
            return this;
        }

        public Builder defaultCstmApplVerId(String defaultCstmApplVerId) {
            this.defaultCstmApplVerId = defaultCstmApplVerId;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder newPassword(String newPassword) {
            this.newPassword = newPassword;
            return this;
        }

        public Builder passwordEncryption(String passwordEncryption) {
            this.passwordEncryption = passwordEncryption;
            return this;
        }

        public Builder test() {
            this.test = true;
            return this;
        }

        public Builder production() {
            this.test = false;
            return this;
        }

        public Builder address(String host, Integer port) {
            this.addresses.add(Tuple.of(host, port));
            return this;
        }

        public Builder heartbeatInterval(Integer heartbeatInterval) {
            this.heartbeatInterval = heartbeatInterval;
            return this;
        }

        public FixSessionConfiguration build() {
            return new FixSessionConfiguration(acceptor, sessionId, defaultApplVerId, defaultApplExtId,
                    defaultCstmApplVerId, username, password, newPassword, passwordEncryption, test, addresses,
                    heartbeatInterval);
        }
    }
}
