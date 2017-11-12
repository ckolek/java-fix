package me.kolek.fix;

import com.google.common.io.ByteProcessor;
import com.google.common.io.Resources;
import me.kolek.fix.util.FixUtil;
import me.kolek.util.tuple.Tuple;
import me.kolek.util.tuple.Tuple2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

public class FixTestUtil {
    public static URL getResource(Class<?> clazz, String name) {
        return Resources.getResource(clazz, name + ".fix");
    }

    public static byte[] getResourceData(Class<?> clazz, String name) throws IOException {
        return Resources.asByteSource(getResource(clazz, name)).read(new ByteProcessor<byte[]>() {
            private final ByteArrayOutputStream out = new ByteArrayOutputStream();

            @Override
            public boolean processBytes(byte[] buf, int off, int len) throws IOException {
                for (int i = off; i < off + len; i++) {
                    byte b = buf[i];
                    out.write(b == '|' ? FixMessage.FIELD_DELIMITER : b);
                }
                return true;
            }

            @Override
            public byte[] getResult() {
                return out.toByteArray();
            }
        });
    }

    public static String getResourceString(Class<?> clazz, String name) throws IOException {
        return new String(getResourceData(clazz, name), FixUtil.DEFAULT_CHARSET);
    }

    public static String[] getResourceComponents(Class<?> clazz, String name) throws IOException {
        String messageString = getResourceString(clazz, name);
        Tuple2<String, Integer> beginString = readFieldValue(messageString, 0, FixUtil.BEGIN_STRING_START);
        Tuple2<String, Integer> bodyLength =
                readFieldValue(messageString, beginString.second() + 1, FixUtil.BODY_LENGTH_START);
        int bodyEndIndex;
        try {
            bodyEndIndex = bodyLength.second() + Integer.parseInt(bodyLength.first());
        } catch (NumberFormatException e) {
            throw new IOException("BodyLength value " + bodyLength.first() + "is not an integer");
        }
        if (bodyEndIndex > messageString.length()) {
            throw new IOException("unexpected end of message");
        }
        String body = messageString.substring(bodyLength.second() + 1, bodyEndIndex);
        Tuple2<String, Integer> checkSum = readFieldValue(messageString, bodyEndIndex + 1, FixUtil.CHECK_SUM_START);
        if (checkSum.second() + 1 != messageString.length()) {
            throw new IOException("unexpected message content after CheckSum");
        }
        return new String[]{beginString.first(), body, checkSum.first()};
    }

    private static Tuple2<String, Integer> readFieldValue(String messageString, int index, String fieldStart)
            throws IOException {
        if (index >= messageString.length()) {
            throw new IOException("unexpected end of message");
        }
        if (!messageString.startsWith(fieldStart, index)) {
            throw new IOException("expected " + fieldStart + " at index " + index);
        }
        int delimIndex = messageString.indexOf(FixMessage.FIELD_DELIMITER, index + fieldStart.length());
        if (delimIndex < 0) {
            throw new IOException("no field delimiter found after " + fieldStart);
        }
        String value = messageString.substring(index + fieldStart.length(), delimIndex);
        if (value.isEmpty()) {
            throw new IOException("value of " + fieldStart + " is empty");
        }
        return Tuple.of(value, delimIndex);
    }
}
