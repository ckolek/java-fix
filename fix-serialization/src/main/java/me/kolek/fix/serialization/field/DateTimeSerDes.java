package me.kolek.fix.serialization.field;

import me.kolek.fix.constants.FieldType;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author ckolek
 */
public class DateTimeSerDes<T extends TemporalAccessor> extends FieldSerDesBase<T> {
    private final TemporalQuery<T> query;
    private final DateTimeFormatter[] formats;

    public DateTimeSerDes(FieldType fieldType, Class<T> valueType, TemporalQuery<T> query, String... formats) {
        super(fieldType, valueType);
        this.query = query;
        this.formats = Arrays.stream(formats).filter(Objects::nonNull).map(DateTimeFormatter::ofPattern)
                .toArray(DateTimeFormatter[]::new);
    }

    @Override
    protected T parseNonNull(String string) {
        T value = null;
        DateTimeParseException e = null;

        for (DateTimeFormatter format : formats) {
            try {
                value = parseValue(string, format);
                break;
            } catch (DateTimeParseException e1) {
                e = e1;
            }
        }

        return value;
    }

    protected T parseValue(String string, DateTimeFormatter format) {
        return format.parse(string, query);
    }

    @Override
    protected String formatNonNull(T value) {
        return formats[0].format(value);
    }
}
