package me.kolek.fix.engine.impl.transport.mina.codec;

import me.kolek.fix.util.FixUtil;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolCodecException;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.nio.charset.Charset;

public class FixMessageDecoder extends CumulativeProtocolDecoder {
    private static final String STATE_KEY = FixMessageDecoder.class.getSimpleName() + ".state";

    private final Charset charset;
    private final byte[] delim;
    private final byte[] beginStringStart;
    private final byte[] bodyLengthStart;
    private final byte[] checksumStart;
    private final int maxIgnoredBytes;

    public FixMessageDecoder(Charset charset, String delim, int maxIgnoredBytes) {
        this.charset = charset;
        this.delim = delim.getBytes(charset);
        this.beginStringStart = FixUtil.BEGIN_STRING_START.getBytes(charset);
        this.bodyLengthStart = FixUtil.BODY_LENGTH_START.getBytes(charset);
        this.checksumStart = FixUtil.CHECK_SUM_START.getBytes(charset);
        this.maxIgnoredBytes = maxIgnoredBytes;
    }

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        boolean decodedMessage = false;
        State state = getState(session);
        state.reposition();
        while (state.decode(session, in, out)) {
            decodedMessage = true;
        }
        if (in.remaining() >= maxIgnoredBytes) {
            throw new ProtocolCodecException(in.remaining() + " bytes on non-decodable data");
        }
        return decodedMessage;
    }

    @Override
    public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {

    }

    @Override
    public void dispose(IoSession session) throws Exception {
        session.removeAttribute(STATE_KEY);
    }

    private State getState(IoSession session) {
        State state = (State) session.getAttribute(STATE_KEY);
        if (state == null) {
            session.setAttribute(STATE_KEY, state = new State());
        }
        return state;
    }

    private class State {
        private int start, position;
        private String beginString;
        private Integer bodyLength;
        private String body;
        private String checkSum;

        public State() {
            reset();
        }

        public void reposition() {
            start = -1;
            position = 0;
        }

        public void reset() {
            start = -1;
            position = 0;
            beginString = null;
            bodyLength = null;
            body = null;
            checkSum = null;
        }

        private boolean decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
            if (in.position() > position) {
                position = in.position();
            }
            if (beginString == null && !readBeginString(session, in)) {
                return false;
            } else if (bodyLength == null && !readBodyLength(session, in)) {
                return false;
            } else if (body == null && !readBody(session, in)) {
                return false;
            } else if (checkSum == null && !readCheckSum(session, in)) {
                return false;
            }
            out.write(new RawFixMessage(beginString, body, checkSum));
            in.position(position);
            reset();
            return true;
        }

        private boolean readBeginString(IoSession session, IoBuffer in) throws Exception {
            return (beginString = readField(in, beginStringStart, true)) != null;
        }

        private boolean readBodyLength(IoSession session, IoBuffer in) throws Exception {
            String bodyLengthString = readField(in, bodyLengthStart, false);
            if (bodyLengthString == null) {
                return false;
            }
            try {
                bodyLength = Integer.valueOf(bodyLengthString);
                return true;
            } catch (NumberFormatException e) {
                throw new ProtocolCodecException(e);
            }
        }

        private boolean readBody(IoSession session, IoBuffer in) throws Exception {
            if (start < 0) {
                start = position;
            }
            int expectedIndex = start + bodyLength - 1;
            int index = find(in, delim, expectedIndex);
            if (index == -2) {
                position = in.limit();
                return false;
            } else if (index != expectedIndex) {
                throw new ProtocolCodecException();
            }
            position = index + 1;
            body = getString(in);
            return true;
        }

        private boolean readCheckSum(IoSession session, IoBuffer in) throws Exception {
            return (checkSum = readField(in, checksumStart, false)) != null;
        }

        private String readField(IoBuffer in, byte[] startBytes, boolean firstField) throws Exception {
            int index;
            if (start < 0) {
                index = find(in, startBytes);
                if (index == -2) {
                    return null;
                } else if (index == -1) {
                    if (!firstField) {
                        throw new ProtocolCodecException();
                    }
                    position = in.limit();
                    return null;
                }
                start = position = index + startBytes.length;
            }
            index = find(in, delim);
            if (index < 0) {
                position = in.limit();
                return null;
            }
            position = index;
            String value = getString(in);
            start = -1;
            in.position(++position);
            return value;
        }

        private int find(IoBuffer buffer, byte[] bytes) {
            return find(buffer, bytes, position);
        }

        private int find(IoBuffer buffer, byte[] bytes, int start) {
            if (buffer.limit() - start < bytes.length) {
                return -2;
            }

            bufferLoop:
            for (int i = start; i <= buffer.limit() - bytes.length; i++) {
                for (int j = 0; j < bytes.length; j++) {
                    if (buffer.get(i + j) != bytes[j]) {
                        continue bufferLoop;
                    }
                }
                return i;
            }
            return -1;
        }

        private String getString(IoBuffer buffer) {
            int length = position - start;
            byte[] bytes = new byte[length];
            buffer.position(start);
            buffer.get(bytes, 0, length);
            return new String(bytes, charset);
        }
    }
}
