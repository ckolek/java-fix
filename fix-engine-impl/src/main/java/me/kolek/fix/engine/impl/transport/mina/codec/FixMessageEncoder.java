package me.kolek.fix.engine.impl.transport.mina.codec;

import me.kolek.fix.util.FixUtil;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.nio.charset.Charset;

public class FixMessageEncoder implements ProtocolEncoder {
    private final Charset charset;
    private final String delim;

    public FixMessageEncoder(Charset charset, String delim) {
        this.charset = charset;
        this.delim = delim;
    }

    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        RawFixMessage fixMessage = (RawFixMessage) message;

        String messageString =
                FixUtil.BEGIN_STRING_START + fixMessage.getBeginString() + delim + FixUtil.BODY_LENGTH_START +
                        fixMessage.getBodyLength() + delim + fixMessage.getBody() + FixUtil.CHECK_SUM_START +
                        fixMessage.getCheckSum() + delim;

        IoBuffer buffer = IoBuffer.wrap(messageString.getBytes(charset));
        buffer.flip();
        out.write(buffer);
    }

    @Override
    public void dispose(IoSession session) throws Exception {
        // do nothing
    }
}
