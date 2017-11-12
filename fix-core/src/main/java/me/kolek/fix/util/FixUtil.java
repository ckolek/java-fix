package me.kolek.fix.util;

import me.kolek.fix.FixMessage;
import me.kolek.fix.TagValue;
import me.kolek.fix.constants.ApplVerId;
import me.kolek.fix.constants.BeginString;
import me.kolek.fix.constants.TagNum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class FixUtil {
    public static final String BEGIN_STRING_START = Integer.toString(TagNum.BeginString) + TagValue.SEPARATOR;
    public static final String BODY_LENGTH_START = Integer.toString(TagNum.BodyLength) + TagValue.SEPARATOR;
    public static final String CHECK_SUM_START = Integer.toString(TagNum.CheckSum) + TagValue.SEPARATOR;

    public static final DateTimeFormatter UTC_DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter UTC_TIME_ONLY_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss[.SSS]");
    public static final DateTimeFormatter UTC_TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss[.SSS]");

    public static Optional<String> toBeginString(String applVerId) {
        return ApplVerId.fromValue(applVerId).map(ApplVerId::getBeginString).map(BeginString::value);
    }

    public static Optional<String> toApplVerId(String beginString) {
        return BeginString.fromValue(beginString).map(BeginString::getApplVerId).map(ApplVerId::value);
    }

    public static String toString(TagValue tagValue) {
        return append(new StringBuilder(), tagValue).toString();
    }

    public static StringBuilder append(StringBuilder string, TagValue tagValue) {
        return string.append(tagValue.getTagNum()).append(TagValue.SEPARATOR).append(tagValue.getValue())
                .append(FixMessage.FIELD_DELIMITER);
    }

    public static StringBuilder insert(StringBuilder string, int index, TagValue tagValue) {
        return string.insert(index, FixMessage.FIELD_DELIMITER).insert(index, tagValue.getValue())
                .insert(index, TagValue.SEPARATOR).insert(index, tagValue.getTagNum());
    }

    public static String toString(Iterable<TagValue> tagValues) {
        return append(new StringBuilder(), tagValues).toString();
    }

    public static StringBuilder append(StringBuilder string, Iterable<TagValue> tagValues) {
        tagValues.forEach(tv -> append(string, tv));
        return string;
    }

    /**
     * Calculates the expected value of the CheckSum(10) field from a raw FIX message. The message should contain the
     * BeginString(8) and BodyLength(9) fields, but should not contain the CheckSum(10) field.
     *
     * @param message the raw FIX message
     * @return the expected CheckSum(10) value
     */
    public static String calculateChecksum(CharSequence message) {
        return String.format("%03d", message.chars().sum() % 256);
    }

    public static String formatUtcDateOnly(LocalDate date) {
        return date.format(UTC_DATE_ONLY_FORMATTER);
    }

    public static String formatUtcTimeOnly(LocalTime time) {
        return time.format(UTC_TIME_ONLY_FORMATTER);
    }

    public static String formatUtcTimestamp(LocalDateTime dateTime) {
        return dateTime.format(UTC_TIMESTAMP_FORMATTER);
    }
}
