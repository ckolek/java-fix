package me.kolek.fix.engine.config;

import me.kolek.fix.engine.FixSessionId;
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
    private final List<Tuple2<String, Integer>> addresses;
    private final Integer heartbeatInterval;

    public FixSessionConfiguration(boolean acceptor, FixSessionId sessionId, String defaultApplVerId, List<Tuple2<String, Integer>> addresses,
            Integer heartbeatInterval) {
        this.acceptor = acceptor;
        this.sessionId = sessionId;
        this.defaultApplVerId = defaultApplVerId;
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
        if (obj == this) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (obj.getClass() != FixSessionConfiguration.class) {
            return false;
        }

        FixSessionConfiguration other = (FixSessionConfiguration) obj;
        return (this.acceptor == other.acceptor) && Objects.equals(this.sessionId, other.sessionId) &&
                Objects.equals(this.defaultApplVerId, other.defaultApplVerId) &&
                Objects.equals(this.addresses, other.addresses) &&
                Objects.equals(this.heartbeatInterval, other.heartbeatInterval);
    }

    public static class Builder {
        private boolean acceptor;
        private FixSessionId sessionId;
        private String defaultApplVerId;
        private final List<Tuple2<String, Integer>> addresses = new ArrayList<>();
        private int heartbeatInterval;

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

        public Builder address(String host, Integer port) {
            this.addresses.add(Tuple.of(host, port));
            return this;
        }

        public Builder heartbeatInterval(int heartbeatInterval) {
            this.heartbeatInterval = heartbeatInterval;
            return this;
        }

        public FixSessionConfiguration build() {
            return new FixSessionConfiguration(acceptor, sessionId, defaultApplVerId, addresses, heartbeatInterval);
        }
    }
}
