package me.kolek.fix.serialization.field;

import me.kolek.fix.constants.FieldType;

/**
 * @author ckolek
 */
public abstract class FieldSerDesBase<T> implements FieldSerDes<T> {
    private final FieldType fieldType;
    private final Class<T> valueType;

    protected FieldSerDesBase(FieldType fieldType, Class<T> valueType) {
        this.fieldType = fieldType;
        this.valueType = valueType;
    }

    @Override
    public FieldType getFieldType() {
        return fieldType;
    }

    @Override
    public Class<T> getValueType() {
        return valueType;
    }

    @Override
    public T parse(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }

        return parseNonNull(string);
    }

    protected abstract T parseNonNull(String string);

    @Override
    public String format(T value) {
        if (value == null) {
            return null;
        }

        return formatNonNull(value);
    }

    protected abstract String formatNonNull(T value);
}
