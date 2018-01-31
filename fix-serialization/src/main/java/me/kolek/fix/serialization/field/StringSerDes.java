package me.kolek.fix.serialization.field;

import me.kolek.fix.constants.FieldType;

/**
 * @author ckolek
 */
public class StringSerDes extends FieldSerDesBase<String> {
    public StringSerDes() {
        this(FieldType.STRING);
    }

    public StringSerDes(FieldType fieldType) {
        super(fieldType, String.class);
    }

    @Override
    protected String parseNonNull(String string) {
        return string;
    }

    @Override
    protected String formatNonNull(String value) {
        return value;
    }
}
