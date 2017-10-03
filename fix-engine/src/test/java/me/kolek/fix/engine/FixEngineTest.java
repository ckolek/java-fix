package me.kolek.fix.engine;

import me.kolek.fix.engine.config.FixEngineConfiguration;
import org.junit.Test;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class FixEngineTest {
    @Test
    public void testEngine() throws Exception {
        Registry registry = LocateRegistry.getRegistry();

        FixEngineFactory factory = (FixEngineFactory) registry.lookup("EngineFactory");

        FixEngine engine = factory.launchEngine(FixEngineConfiguration.build(ec -> ec.session(
                sc -> sc.initiator().sessionId("1").address("localhost", 7010).beginString("FIX.4.0")
                        .targetCompId("SELL40").senderCompId("BUY40").heartbeatInterval(30)).session(
                sc -> sc.initiator().sessionId("2").address("localhost", 7011).beginString("FIX.4.2")
                        .targetCompId("SELL42").senderCompId("BUY42").heartbeatInterval(30)).session(
                sc -> sc.initiator().sessionId("3").address("localhost", 7012).beginString("FIX.4.4")
                        .targetCompId("SELL44").senderCompId("BUY44").heartbeatInterval(30))));

        FixSession sell44 = engine.getSession("3");
        sell44.logon();
    }
}
