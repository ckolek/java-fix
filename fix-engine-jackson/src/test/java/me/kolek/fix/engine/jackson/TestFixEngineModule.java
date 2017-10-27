package me.kolek.fix.engine.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kolek.fix.constants.BeginString;
import me.kolek.fix.engine.config.FixEngineConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class TestFixEngineModule {
    private ObjectMapper mapper;
    private FixEngineConfiguration configuration;
    private File configFile;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        configuration = FixEngineConfiguration.build(engine -> {
            engine.acceptorHost("engine-host1").acceptorPort(8001);
            engine.session(session -> {
                session.acceptor().sessionId(id -> id.beginString(BeginString._FIXT11).sender("SENDR44", "DESK1", "1F")
                        .target("RECV44", "DESK2", "0A")).defaultApplVerId("6").address("localhost", 8002)
                        .address("localhost", 8003);
            });
        });

        configFile = File.createTempFile("fix", null);
    }

    @Test
    public void testModule() throws Exception {
        mapper.writeValue(configFile, configuration);

        FixEngineConfiguration configuration = mapper.readValue(configFile, FixEngineConfiguration.class);

        try {
            assertEquals(this.configuration, configuration);
        } catch (AssertionError error) {
            assertEquals(mapper.writeValueAsString(this.configuration), mapper.writeValueAsString(configuration));
        }
    }
}
