package me.kolek.fix.engine.impl.transport.mina.codec;

import me.kolek.fix.FixMessage;
import me.kolek.fix.FixTestUtil;
import me.kolek.fix.util.FixUtil;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.DefaultTransportMetadata;
import org.apache.mina.core.session.DummySession;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FixMessageDecoderTest {
    private byte[] data;
    private IoSession session;
    private FixMessageDecoder decoder;
    private DecoderOutput output;

    @Before
    public void setUp() throws Exception {
        data = FixTestUtil.getResourceData(getClass(), "test_message");

        DummySession session = new DummySession();
        DefaultTransportMetadata metadata = (DefaultTransportMetadata) session.getTransportMetadata();
        metadata = new DefaultTransportMetadata(metadata.getProviderName(), metadata.getName(),
                metadata.isConnectionless(), true, metadata.getAddressType(), metadata.getSessionConfigType(),
                metadata.getEnvelopeTypes().toArray(new Class[0]));
        session.setTransportMetadata(metadata);
        this.session = session;

        decoder = new FixMessageDecoder(FixUtil.DEFAULT_CHARSET, Character.toString(FixMessage.FIELD_DELIMITER), 4096);
        output = new DecoderOutput();
    }

    @Test
    public void testDecode() throws Exception {
        IoBuffer buffer = IoBuffer.allocate(10);
        for (int i = 0; i < data.length; i += 10) {
            buffer.clear();
            buffer.put(data, i, Math.min(10, data.length - i));
            buffer.flip();
            decoder.decode(session, buffer, output);
        }

        assertEquals(1, output.messages.size());

        RawFixMessage rawFixMessage = (RawFixMessage) output.messages.get(0);
        assertEquals("FIX.4.4", rawFixMessage.getBeginString());
        assertEquals(new String(data).substring(16, data.length - 7), rawFixMessage.getBody());
        assertEquals("072", rawFixMessage.getCheckSum());
    }

    @After
    public void tearDown() throws Exception {
        decoder.dispose(session);
    }

    private static class DecoderOutput implements ProtocolDecoderOutput {
        private final List<Object> messages = new ArrayList<>();

        @Override
        public void write(Object message) {
            messages.add(message);
        }

        @Override
        public void flush(IoFilter.NextFilter nextFilter, IoSession session) {

        }
    }
}
