package me.kolek.fix.serialization.metadata;

import me.kolek.fix.TagValue;
import me.kolek.fix.serialization.field.FieldSerDes;

/**
 * @author ckolek
 */
public class FieldValue<T> {
    private final int tagNum;
    private final FieldSerDes<T> serDes;

    private String stringValue;
    private T value;

    private FieldValue(int tagNum, FieldSerDes<T> serDes, String stringValue, T value) {
        this.tagNum = tagNum;
        this.serDes = serDes;
        this.stringValue = stringValue;
        this.value = value;
    }

    public static <T> FieldValue<T> raw(int tagNum, FieldSerDes<T> serDes, String stringValue) {
        return new FieldValue<>(tagNum, serDes, stringValue, null);
    }

    public static <T> FieldValue<T> withValue(int tagNum, FieldSerDes<T> serDes, T value) {
        return new FieldValue<>(tagNum, serDes, null, value);
    }

    public int getTagNum() {
        return tagNum;
    }

    public String getStringValue() {
        if (stringValue == null) {
            stringValue = serDes.format(value);
        }
        return stringValue;
    }

    public T getValue() {
        if (value == null) {
            value = serDes.parse(stringValue);
        }
        return value;
    }

    public TagValue toTagValue() {
        return new TagValue(tagNum, getStringValue());
    }
}
