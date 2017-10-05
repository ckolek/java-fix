package me.kolek.fix.util;

import me.kolek.fix.FixDictionary;
import me.kolek.fix.FixMessage;
import me.kolek.fix.TagValue;
import me.kolek.fix.constants.Field;
import me.kolek.util.exception.CannotHappenException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FixMessageParser {
    private final FixDictionary dictionary;
    private final ThreadLocal<ByteArrayOutputStream> buffers;

    public FixMessageParser(FixDictionary dictionary) {
        this.dictionary = dictionary;
        this.buffers = ThreadLocal.withInitial(ByteArrayOutputStream::new);
    }

    public FixMessage parse(String message) {
        try {
            return parse(new ByteArrayInputStream(message.getBytes()));
        } catch (IOException e) {
            throw new CannotHappenException(e);
        }
    }

    public FixMessage parse(InputStream inputStream) throws IOException {
        List<TagValue> tagValues = new ArrayList<>();
        int tagNum;
        String value;
        int length = -1;
        while ((tagNum = parseTagNum(inputStream)) != Integer.MIN_VALUE) {
            value = parseValue(inputStream, length);
            if (dictionary.getFieldType(tagNum) == Field.FieldType.LENGTH) {
                length = Integer.parseInt(value);
            } else {
                length = -1;
            }
            tagValues.add(new TagValue(tagNum, value));
        }
        return new FixMessage(tagValues);
    }

    private int parseTagNum(InputStream inputStream) throws IOException {
        int c = inputStream.read();
        if (c == -1) {
            return Integer.MIN_VALUE;
        }

        ByteArrayOutputStream buffer = buffers.get();
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

    private String parseValue(InputStream inputStream, int length) throws IOException {
        if (length == 0) {
            return "";
        }

        ByteArrayOutputStream buffer = buffers.get();
        buffer.reset();

        int c;
        while (true) {
            c = inputStream.read();
            if (c < 0) {
                throw new IOException("unexpected end of stream");
            }
            if (length < 0 && c == FixMessage.FIELD_DELIMITER) {
                break;
            }
            buffer.write(c);
            if (length > 0 && buffer.size() == length) {
                break;
            }
        }

        return new String(buffer.toByteArray());
    }
}
