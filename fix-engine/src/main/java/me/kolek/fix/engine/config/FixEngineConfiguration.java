package me.kolek.fix.engine.config;

import me.kolek.fix.engine.FixSessionId;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FixEngineConfiguration implements Serializable {
    private final String acceptorHost;
    private final Integer acceptorPort;

    private final Map<FixSessionId, FixSessionConfiguration> sessions;

    public FixEngineConfiguration(String acceptorHost, Integer acceptorPort,
            Collection<FixSessionConfiguration> sessions) {
        this.acceptorHost = acceptorHost;
        this.acceptorPort = acceptorPort;
        this.sessions = Collections.unmodifiableMap(sessions.stream()
                .collect(Collectors.toMap(FixSessionConfiguration::getSessionId, Function.identity())));
    }

    public static FixEngineConfiguration build(Consumer<FixEngineConfiguration.Builder> builderConsumer) {
        Builder builder = new Builder();
        builderConsumer.accept(builder);
        return builder.build();
    }

    public String getAcceptorHost() {
        return acceptorHost;
    }

    public Integer getAcceptorPort() {
        return acceptorPort;
    }

    public Collection<FixSessionConfiguration> getSessions() {
        return sessions.values();
    }

    public FixSessionConfiguration getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(acceptorHost, acceptorPort, sessions);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (obj.getClass() != FixEngineConfiguration.class) {
            return false;
        }

        FixEngineConfiguration other = (FixEngineConfiguration) obj;
        return Objects.equals(this.acceptorHost, other.acceptorHost) &&
                Objects.equals(this.acceptorPort, other.acceptorPort) && this.sessions.equals(other.sessions);
    }

    public static class Builder {
        private final List<FixSessionConfiguration> sessions = new ArrayList<>();
        private String acceptorHost;
        private Integer acceptorPort;

        public Builder acceptorHost(String acceptorHost) {
            this.acceptorHost = acceptorHost;
            return this;
        }

        public Builder acceptorPort(Integer acceptorPort) {
            this.acceptorPort = acceptorPort;
            return this;
        }

        public Builder session(Consumer<FixSessionConfiguration.Builder> builderConsumer) {
            return session(FixSessionConfiguration.build(builderConsumer));
        }

        public Builder session(FixSessionConfiguration session) {
            sessions.add(session);
            return this;
        }

        public FixEngineConfiguration build() {
            return new FixEngineConfiguration(acceptorHost, acceptorPort, sessions);
        }
    }
}
