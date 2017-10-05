package me.kolek.fix.engine;

import java.io.Serializable;
import java.util.function.Consumer;

public class FixSessionId implements Serializable {
    private final String beginString;
    private final String senderCompId;
    private final String senderSubId;
    private final String senderLocationId;
    private final String targetCompId;
    private final String targetSubId;
    private final String targetLocationId;

    public FixSessionId(String beginString, String senderCompId, String senderSubId, String senderLocationId, String targetCompId,
            String targetSubId, String targetLocationId) {
        this.beginString = beginString;
        this.senderCompId = senderCompId;
        this.senderSubId = senderSubId;
        this.senderLocationId = senderLocationId;
        this.targetCompId = targetCompId;
        this.targetSubId = targetSubId;
        this.targetLocationId = targetLocationId;
    }

    public String getBeginString() {
        return beginString;
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

    public static FixSessionId build(Consumer<Builder> builderConsumer) {
        Builder builder = new Builder();
        builderConsumer.accept(builder);
        return builder.build();
    }

    public static class Builder {
        private String beginString;
        private String senderCompId;
        private String senderSubId;
        private String senderLocationId;
        private String targetCompId;
        private String targetSubId;
        private String targetLocationId;

        public Builder beginString(String beginString) {
            this.beginString = beginString;
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

        public FixSessionId build() {
            return new FixSessionId(beginString, senderCompId, senderSubId, senderLocationId, targetCompId, targetSubId,
                    targetLocationId);
        }
    }
}
