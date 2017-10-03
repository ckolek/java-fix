package me.kolek.fix.util;

import me.kolek.fix.FixMessage;
import me.kolek.fix.TagValue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FixMessageParser {
    private final ByteArrayOutputStream buffer;

    public FixMessageParser() {
        buffer = new ByteArrayOutputStream();
    }

    public FixMessage parse(String message) throws IOException {
        return parse(new ByteArrayInputStream(message.getBytes()));
    }

    public FixMessage parse(InputStream inputStream) throws IOException {
        List<TagValue> tagValues = new ArrayList<>();
        int tagNum;
        String value;
        while ((tagNum = parseTagNum(inputStream)) != Integer.MIN_VALUE) {
            value = parseValue(inputStream);
            tagValues.add(new TagValue(tagNum, value));
        }
        return new FixMessage(tagValues);
    }

    private int parseTagNum(InputStream inputStream) throws IOException {
        int c = inputStream.read();
        if (c == -1) {
            return Integer.MIN_VALUE;
        }

        buffer.reset();
        buffer.write(c);

        while ((c = inputStream.read()) != TagValue.SEPARATOR) {
            if (c < 0) {
                throw new IOException("unexpected end of stream");
            }
            buffer.write(c);
        }

        return Integer.parseInt(new String(buffer.toByteArray()));
    }

    private String parseValue(InputStream inputStream) throws IOException {
        int c = inputStream.read();
        if (c == -1) {
            throw new IOException("unexpected end of stream");
        }

        buffer.reset();
        buffer.write(c);

        while ((c = inputStream.read()) != FixMessage.FIELD_DELIMITER) {
            if (c < 0) {
                throw new IOException("unexpected end of stream");
            }
            buffer.write(c);
        }

        return new String(buffer.toByteArray());
    }
}
