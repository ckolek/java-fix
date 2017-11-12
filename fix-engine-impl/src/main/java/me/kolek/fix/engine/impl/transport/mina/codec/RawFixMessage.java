package me.kolek.fix.engine.impl.transport.mina.codec;

import me.kolek.fix.FixMessage;
import me.kolek.fix.util.FixUtil;

public class RawFixMessage {
    private final String beginString;
    private final String body;
    private final String checkSum;

    public RawFixMessage(String beginString, String body, String checkSum) {
        this.beginString = beginString;
        this.body = body;
        this.checkSum = checkSum;
    }

    public String getBeginString() {
        return beginString;
    }

    public int getBodyLength() {
        return body.length();
    }

    public String getBody() {
        return body;
    }

    public String getCheckSum() {
        return checkSum;
    }

    @Override
    public String toString() {
        return FixUtil.BEGIN_STRING_START + beginString + FixMessage.FIELD_DELIMITER + FixUtil.BODY_LENGTH_START +
                body.length() + FixMessage.FIELD_DELIMITER + body + FixUtil.CHECK_SUM_START + checkSum +
                FixMessage.FIELD_DELIMITER;
    }
}
