package me.kolek.fix.engine.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import me.kolek.fix.engine.FixSessionId;
import me.kolek.fix.engine.config.FixSessionConfiguration;

import java.io.IOException;
import java.util.List;

public class FixSessionConfigurationDeserializer extends JsonDeserializer<FixSessionConfiguration> {
    public static final FixSessionConfigurationDeserializer INSTANCE = new FixSessionConfigurationDeserializer();

    @Override
    public FixSessionConfiguration deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        DelegateBuilder builder = ctxt.readValue(p, DelegateBuilder.class);
        return builder.delegate.build();
    }

    private static class DelegateBuilder {
        private final FixSessionConfiguration.Builder delegate = new FixSessionConfiguration.Builder();

        public void setAcceptor(boolean acceptor) {
            if (acceptor) {
                delegate.acceptor();
            } else {
                delegate.initiator();
            }
        }

        public void setSessionId(FixSessionId sessionId) {
            delegate.sessionId(sessionId);
        }

        public void setDefaultApplVerId(String defaultApplVerId) {
            delegate.defaultApplVerId(defaultApplVerId);
        }

        public void setAddresses(List<Address> addresses) {
            addresses.forEach(address -> delegate.address(address.host, address.port));
        }

        public void setHeartbeatInterval(Integer heartbeatInterval) {
            delegate.heartbeatInterval(heartbeatInterval);
        }
    }

    private static class Address {
        private String host;
        private Integer port;

        public void setHost(String host) {
            this.host = host;
        }

        public void setPort(Integer port) {
            this.port = port;
        }
    }
}
