package me.kolek.fix.serialization.field;

/**
 * @author ckolek
 */
public abstract class FieldSerDesBase<T> implements FieldSerDes<T> {
    private final Class<T> valueType;

    protected FieldSerDesBase(Class<T> valueType) {
        this.valueType = valueType;
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
