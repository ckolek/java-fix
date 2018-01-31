package me.kolek.fix.engine;

import me.kolek.fix.constants.BeginString;
import me.kolek.fix.engine.config.FixEngineConfiguration;
import me.kolek.fix.engine.runtime.UnicastFixEngineCallback;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.CountDownLatch;

public class FixEngineTest {
//    @Test
    public void testEngine() throws Exception {
        Registry registry = LocateRegistry.getRegistry();

        FixEngineFactory factory = (FixEngineFactory) registry.lookup("EngineFactory");

        CountDownLatch latch = new CountDownLatch(3);

        FixSessionId sell40Id = FixSessionId
                .build(id -> id.beginString(BeginString._FIX40).senderCompId("BUY40").targetCompId("SELL40"));
        FixSessionId sell42Id = FixSessionId
                .build(id -> id.beginString(BeginString._FIX42).senderCompId("BUY42").targetCompId("SELL42"));
        FixSessionId sell44Id = FixSessionId
                .build(id -> id.beginString(BeginString._FIX44).senderCompId("BUY44").targetCompId("SELL44"));

        FixEngine engine = factory.launchEngine(FixEngineConfiguration.build(ec -> ec
                        .session(sc -> sc.initiator().sessionId(sell40Id).address("localhost", 7010)
                                .heartbeatInterval(30))
                        .session(sc -> sc.initiator().sessionId(sell42Id).address("localhost", 7011)
                                .heartbeatInterval(30))
                        .session(sc -> sc.initiator().sessionId(sell44Id).address("localhost", 7012)
                                .heartbeatInterval(30))),
                null, new UnicastFixEngineCallback() {
                    @Override
                    public void onSessionAvailable(FixSessionId sessionId) throws RemoteException {
                        latch.countDown();
                    }
                });

        latch.await();

        FixSession sell44 = engine.getSession(sell44Id);
        sell44.logon();
    }
}
