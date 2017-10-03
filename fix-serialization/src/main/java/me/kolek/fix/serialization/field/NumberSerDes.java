package me.kolek.fix.serialization.field;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author ckolek
 */
public abstract class NumberSerDes<N extends Number> extends FieldSerDesBase<N> {
    private final ThreadLocal<NumberFormat>[] formats;

    public NumberSerDes(Class<N> valueType, Double multiplier, String... formats) {
        super(valueType);
        this.formats = Arrays.stream(formats).filter(Objects::nonNull)
                .map(format -> ThreadLocal.withInitial(() -> new DecimalFormat(format))).toArray(ThreadLocal[]::new);
    }

    @Override
    protected N parseNonNull(String string) {
        N value = null;
        Exception e = null;

        if (formats.length > 0) {
            for (ThreadLocal<NumberFormat> format : formats) {
                try {
                    value = parseValue(string, format.get());
                    break;
                } catch (ParseException e1) {
                    e = e1;
                }
            }
        } else {
            try {
                value = parseValue(string);
            } catch (NumberFormatException e1) {
                e = e1;
            }
        }

        return value;
    }

    protected abstract N parseValue(String string) throws NumberFormatException;

    protected abstract N parseValue(String string, NumberFormat format) throws ParseException;

    @Override
    protected String formatNonNull(N value) {
        if (formats.length > 0) {
            return formatValue(value, formats[0].get());
        } else {
            return formatValue(value);
        }
    }

    protected abstract String formatValue(N value);

    protected abstract String formatValue(N value, NumberFormat format) throws IllegalArgumentException;

    protected abstract N multiply(N value, double multiplier);
}
