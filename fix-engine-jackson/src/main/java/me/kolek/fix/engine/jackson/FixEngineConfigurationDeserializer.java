package me.kolek.fix.engine.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import me.kolek.fix.engine.config.FixEngineConfiguration;
import me.kolek.fix.engine.config.FixSessionConfiguration;

import java.io.IOException;
import java.util.List;

public class FixEngineConfigurationDeserializer extends JsonDeserializer<FixEngineConfiguration> {
    public static final FixEngineConfigurationDeserializer INSTANCE = new FixEngineConfigurationDeserializer();

    @Override
    public FixEngineConfiguration deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        return null;
    }

    private static class DelegateBuilder {
        FixEngineConfiguration.Builder delegate = new FixEngineConfiguration.Builder();

        public void setAcceptorHost(String acceptorHost) {
            delegate.acceptorHost(acceptorHost);
        }

        public void setAcceptorPort(Integer acceptorPort) {
            delegate.acceptorPort(acceptorPort);
        }

        public void setSessions(List<FixSessionConfiguration> sessions) {
            sessions.forEach(delegate::session);
        }
    }
}
