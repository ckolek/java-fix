package me.kolek.fix.engine.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import me.kolek.fix.engine.FixSessionId;

import java.io.IOException;

public class FixSessionIdDeserializer extends JsonDeserializer<FixSessionId> {
    public static final FixSessionIdDeserializer INSTANCE = new FixSessionIdDeserializer();

    @Override
    public FixSessionId deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        DelegateBuilder builder = ctxt.readValue(p, DelegateBuilder.class);
        return builder.delegate.build();
    }

    private static class DelegateBuilder {
        private final FixSessionId.Builder delegate = new FixSessionId.Builder();

        public void setBeginString(String beginString) {
            delegate.beginString(beginString);
        }

        public void setSenderCompId(String senderCompId) {
            delegate.senderCompId(senderCompId);
        }

        public void setSenderSubId(String senderSubId) {
            delegate.senderSubId(senderSubId);
        }

        public void setSenderLocationId(String senderLocationId) {
            delegate.senderLocationId(senderLocationId);
        }

        public void setTargetCompId(String targetCompId) {
            delegate.targetCompId(targetCompId);
        }

        public void setTargetSubId(String targetSubId) {
            delegate.targetSubId(targetSubId);
        }

        public void setTargetLocationId(String targetSubId) {
            delegate.targetLocationId(targetSubId);
        }
    }
}
