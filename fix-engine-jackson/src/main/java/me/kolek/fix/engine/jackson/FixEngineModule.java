package me.kolek.fix.engine.jackson;

import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.databind.module.SimpleModule;
import me.kolek.fix.engine.FixSessionId;
import me.kolek.fix.engine.config.FixEngineConfiguration;
import me.kolek.fix.engine.config.FixSessionConfiguration;

public final class FixEngineModule extends SimpleModule {
    public FixEngineModule() {
        super(PackageVersion.VERSION);

        addSerializer(FixSessionConfiguration.class, FixSessionConfigurationSerializer.INSTANCE);

        addDeserializer(FixEngineConfiguration.class, FixEngineConfigurationDeserializer.INSTANCE);
        addDeserializer(FixSessionConfiguration.class, FixSessionConfigurationDeserializer.INSTANCE);
        addDeserializer(FixSessionId.class, FixSessionIdDeserializer.INSTANCE);
    }
}
