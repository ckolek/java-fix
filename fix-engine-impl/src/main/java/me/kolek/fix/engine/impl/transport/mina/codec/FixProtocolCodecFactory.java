package me.kolek.fix.engine.impl.transport.mina.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import java.nio.charset.Charset;

public class FixProtocolCodecFactory implements ProtocolCodecFactory {
    private final FixMessageEncoder encoder;
    private final FixMessageDecoder decoder;

    public FixProtocolCodecFactory(Charset charset, String delim, int maxIgnoredBytes) {
        this.encoder = new FixMessageEncoder(charset, delim);
        this.decoder = new FixMessageDecoder(charset, delim, maxIgnoredBytes);
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }
}
