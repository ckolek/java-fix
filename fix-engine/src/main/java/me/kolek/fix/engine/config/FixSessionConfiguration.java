package me.kolek.fix.engine.config;

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
    private final String sessionId;
    private final String beginString;
    private final String defaultApplVerId;
    private final String senderCompId;
    private final String senderSubId;
    private final String senderLocationId;
    private final String targetCompId;
    private final String targetSubId;
    private final String targetLocationId;
    private final List<Tuple2<String, Integer>> addresses;
    private final Integer heartbeatInterval;

    public FixSessionConfiguration(boolean acceptor, String sessionId, String beginString, String defaultApplVerId,
            String senderCompId, String senderSubId, String senderLocationId, String targetCompId, String targetSubId,
            String targetLocationId, List<Tuple2<String, Integer>> addresses, Integer heartbeatInterval) {
        this.acceptor = acceptor;
        this.sessionId = sessionId;
        this.beginString = beginString;
        this.defaultApplVerId = defaultApplVerId;
        this.targetCompId = targetCompId;
        this.targetSubId = targetSubId;
        this.targetLocationId = targetLocationId;
        this.senderCompId = senderCompId;
        this.senderSubId = senderSubId;
        this.senderLocationId = senderLocationId;
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

    public String getSessionId() {
        return sessionId;
    }

    public String getBeginString() {
        return beginString;
    }

    public String getDefaultApplVerId() {
        return defaultApplVerId;
    }

    public String getSenderCompId() {
        return senderCompId;
    }

    public String getSenderSubId() {
        return senderSubId;
    }

    public String getSenderLocationId() {
        return senderLocationId;
    }

    public String getTargetCompId() {
        return targetCompId;
    }

    public String getTargetSubId() {
        return targetSubId;
    }

    public String getTargetLocationId() {
        return targetLocationId;
    }

    public List<Tuple2<String, Integer>> getAddresses() {
        return addresses;
    }

    public Integer getHeartbeatInterval() {
        return heartbeatInterval;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, beginString, defaultApplVerId, senderCompId, senderSubId, senderLocationId,
                targetCompId, targetSubId, targetLocationId, addresses, heartbeatInterval);
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
                Objects.equals(this.beginString, other.beginString) &&
                Objects.equals(this.defaultApplVerId, other.defaultApplVerId) &&
                Objects.equals(this.senderCompId, other.senderCompId) &&
                Objects.equals(this.senderSubId, other.senderSubId) &&
                Objects.equals(this.senderLocationId, other.senderLocationId) &&
                Objects.equals(this.targetCompId, other.targetCompId) &&
                Objects.equals(this.targetSubId, other.targetSubId) &&
                Objects.equals(this.targetLocationId, other.targetLocationId) &&
                Objects.equals(this.addresses, other.addresses) &&
                Objects.equals(this.heartbeatInterval, other.heartbeatInterval);
    }

    public static class Builder {
        private final List<Tuple2<String, Integer>> addresses = new ArrayList<>();
        private boolean acceptor;
        private String sessionId;
        private String beginString;
        private String defaultApplVerId;
        private String senderCompId;
        private String senderSubId;
        private String senderLocationId;
        private String targetCompId;
        private String targetSubId;
        private String targetLocationId;
        private int heartbeatInterval;

        public Builder acceptor() {
            this.acceptor = true;
            return this;
        }

        public Builder initiator() {
            this.acceptor = false;
            return this;
        }

        public Builder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public Builder beginString(String beginString) {
            this.beginString = beginString;
            return this;
        }

        public Builder defaultApplVerId(String defaultApplVerId) {
            this.defaultApplVerId = defaultApplVerId;
            return this;
        }

        public Builder senderCompId(String senderCompId) {
            this.senderCompId = senderCompId;
            return this;
        }

        public Builder senderSubId(String senderSubId) {
            this.senderSubId = senderSubId;
            return this;
        }

        public Builder senderLocationId(String senderLocationId) {
            this.senderLocationId = senderLocationId;
            return this;
        }

        public Builder targetCompId(String targetCompId) {
            this.targetCompId = targetCompId;
            return this;
        }

        public Builder targetSubId(String targetSubId) {
            this.targetSubId = targetSubId;
            return this;
        }

        public Builder targetLocationId(String targetLocationId) {
            this.targetLocationId = targetLocationId;
            return this;
        }

        public Builder address(String host, Integer port) {
            addresses.add(Tuple.of(host, port));
            return this;
        }

        public Builder heartbeatInterval(int heartbeatInterval) {
            this.heartbeatInterval = heartbeatInterval;
            return this;
        }

        public FixSessionConfiguration build() {
            return new FixSessionConfiguration(acceptor, sessionId, beginString, defaultApplVerId, senderCompId,
                    senderSubId, senderLocationId, targetCompId, targetSubId, targetLocationId, addresses,
                    heartbeatInterval);
        }
    }
}
