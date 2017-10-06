package me.kolek.fix.engine.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import me.kolek.fix.engine.config.FixSessionConfiguration;
import me.kolek.util.tuple.Tuple2;

import java.io.IOException;

public class FixSessionConfigurationSerializer extends JsonSerializer<FixSessionConfiguration> {
    public static final FixSessionConfigurationSerializer INSTANCE = new FixSessionConfigurationSerializer();

    @Override
    public void serialize(FixSessionConfiguration value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeBooleanField("acceptor", value.isAcceptor());
        gen.writeObjectField("sessionId", value.getSessionId());
        gen.writeStringField("defaultApplVerId", value.getDefaultApplVerId());
        gen.writeArrayFieldStart("addresses");
        for (Tuple2<String, Integer> address : value.getAddresses()) {
            gen.writeStartObject();
            gen.writeStringField("host", address.first());
            if (address.second() != null) {
                gen.writeNumberField("port", address.second());
            }
            gen.writeEndObject();
        }
        gen.writeEndArray();
        if (value.getHeartbeatInterval() != null) {
            gen.writeObjectField("heartbeatInterval", value.getHeartbeatInterval());
        }
    }
}
