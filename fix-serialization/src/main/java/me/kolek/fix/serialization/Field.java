package me.kolek.fix.serialization;

import me.kolek.fix.TagValue;
import me.kolek.fix.serialization.metadata.FieldMetadata;

/**
 * @author ckolek
 */
public class Field<T> {
    private final FieldMetadata<T> metadata;

    private String stringValue;
    private T value;

    private Field(FieldMetadata<T> metadata, String stringValue, T value) {
        this.metadata = metadata;
        this.stringValue = stringValue;
        this.value = value;
    }

    public static <T> Field<T> raw(FieldMetadata<T> metadata, String stringValue) {
        return new Field<>(metadata, stringValue, null);
    }

    public static <T> Field<T> withValue(FieldMetadata<T> metadata, T value) {
        return new Field<>(metadata, null, value);
    }

    public static Field<String> udf(int tagNum, String value) {
        return new Field<>(FieldMetadata.udf(tagNum), value, value);
    }

    public FieldMetadata<T> getMetadata() {
        return metadata;
    }

    public String getStringValue() {
        if (stringValue == null) {
            stringValue = metadata.getSerDes().format(value);
        }
        return stringValue;
    }

    public T getValue() {
        if (value == null) {
            value = metadata.getSerDes().parse(stringValue);
        }
        return value;
    }

    public TagValue toTagValue() {
        return new TagValue(metadata.getTagNum(), getStringValue());
    }
}
