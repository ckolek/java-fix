package me.kolek.fix.engine.impl.transport.mina.codec;

import me.kolek.fix.FixMessage;
import me.kolek.fix.FixTestUtil;
import me.kolek.fix.util.FixUtil;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.DefaultWriteFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.DummySession;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FixMessageEncoderTest {
    private IoSession session;
    private FixMessageEncoder encoder;
    private EncoderOutput output;

    @Before
    public void setUp() {
        session = new DummySession();
        encoder = new FixMessageEncoder(FixUtil.DEFAULT_CHARSET, Character.toString(FixMessage.FIELD_DELIMITER));
        output = new EncoderOutput(session);
    }

    @Test
    public void testEncode() throws Exception {
        String[] components = FixTestUtil.getResourceComponents(getClass(), "test_message");

        RawFixMessage message = new RawFixMessage(components[0], components[1], components[2]);

        encoder.encode(session, message, output);

        assertEquals(1, output.messages.size());
        IoBuffer data = (IoBuffer) output.messages.get(0);
        assertEquals(message.toString(), new String(data.array(), FixUtil.DEFAULT_CHARSET));
    }

    private static class EncoderOutput implements ProtocolEncoderOutput {
        private final IoSession session;
        private final List<Object> messages = new ArrayList<>();

        private EncoderOutput(IoSession session) {
            this.session = session;
        }

        @Override
        public void write(Object encodedMessage) {
            messages.add(encodedMessage);
        }

        @Override
        public void mergeAll() {

        }

        @Override
        public WriteFuture flush() {
            DefaultWriteFuture future = new DefaultWriteFuture(session);
            future.setWritten();
            return future;
        }
    }
}
