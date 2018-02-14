package me.kolek.fix.serialization;

import me.kolek.fix.FixMessage;
import me.kolek.fix.FixTestUtil;
import me.kolek.fix.TagValue;
import me.kolek.fix.serialization.metadata.MessageMetadata;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author ckolek
 */
public class TestFixSerialization {
    @Test
    public void testSerialization() throws Exception {
        MessageMetadata messageMetadata =
                StructureMetadataBuilder.getMetadataResource(getClass(), "TestFixSerialization_message1");

        FixMessage rawMessage = FixTestUtil.getResourceMessage(getClass(), "TestFixSerialization_message1");

        Message message = messageMetadata.deserialize(rawMessage);
        List<TagValue> tagValues = message.toTagValues();
        assertEquals(rawMessage, new FixMessage(tagValues));
    }

    public static TagValuesBuilder tagValues() {
        return new TagValuesBuilder();
    }

    public static class TagValuesBuilder {
        private final List<TagValue> tagValues;

        public TagValuesBuilder() {
            this.tagValues = new ArrayList<>();
        }

        public TagValuesBuilder add(int tagNum, boolean value) {
            return add(tagNum, value ? "Y" : "N");
        }

        public TagValuesBuilder add(int tagNum, char value) {
            return add(tagNum, Character.toString(value));
        }

        public TagValuesBuilder add(int tagNum, char... values) {
            StringBuilder string = new StringBuilder();
            for (int i = 0; i < values.length; i++) {
                string.append(values[i]);
                if (i < values.length - 1) {
                    string.append(' ');
                }
            }
            return add(tagNum, string.toString());
        }

        public TagValuesBuilder add(int tagNum, double value) {
            return add(tagNum, Double.toString(value));
        }

        public TagValuesBuilder add(int tagNum, float value) {
            return add(tagNum, Float.toString(value));
        }

        public TagValuesBuilder add(int tagNum, int value) {
            return add(tagNum, Integer.toString(value));
        }

        public TagValuesBuilder add(int tagNum, long value) {
            return add(tagNum, Long.toString(value));
        }

        public TagValuesBuilder add(int tagNum, short value) {
            return add(tagNum, Short.toString(value));
        }

        public TagValuesBuilder add(int tagNum, String value) {
            tagValues.add(new TagValue(tagNum, value));
            return this;
        }

        public TagValuesBuilder add(int tagNum, String... values) {
            return add(tagNum, String.join(" ", values));
        }

        public TagValuesBuilder add(int tagNum, LocalDate value) {
            return add(tagNum, value, "yyyyMMdd");
        }

        public TagValuesBuilder add(int tagNum, LocalTime value) {
            return add(tagNum, value, "HH:mm:ss.nnnnnn");
        }

        public TagValuesBuilder add(int tagNum, LocalDateTime value) {
            return add(tagNum, value, "yyyyMMdd-HH:mm:ss.nnnnnn");
        }

        public TagValuesBuilder add(int tagNum, Temporal value, String format) {
            return add(tagNum, value, DateTimeFormatter.ofPattern(format));
        }

        public TagValuesBuilder add(int tagNum, Temporal value, DateTimeFormatter formatter) {
            return add(tagNum, formatter.format(value));
        }

        public List<TagValue> build() {
            return tagValues;
        }
    }
}
