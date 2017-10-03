package me.kolek.fix.serialization.field;

import me.kolek.fix.constants.Field;

import java.time.temporal.TemporalAccessor;

/**
 * @author ckolek
 */
public interface FieldSerDes<T> {
    static FieldSerDes<?> getInstance(Field.FieldType fieldType, String[] formats, Double multiplier) {
        switch (fieldType) {
            case BOOLEAN:
                return new BooleanSerDes();
            case CHAR:
                return new CharSerDes();
            case DATA:
                return new DataSerDes();
            case DATETIME:
                return new DateTimeSerDes<>(TemporalAccessor.class, t -> t, formats);
            case FLOAT:
                return new FloatSerDes(multiplier, formats);
            case INT:
                return new IntSerDes(multiplier, formats);
            case LOCALMKTDATE:
                return new LocalMktDateSerDes(formats);
            case MONTHYEAR:
                return new MonthYearSerDes(formats);
            case MULTIPLECHARVALUE:
                return new MultipleCharValueSerDes();
            case MULTIPLESTRINGVALUE:
                return new MultipleStringValueSerDes();
            case STRING:
                return new StringSerDes();
            case TZTIMEONLY:
                return new TzTimeOnlySerDes(formats);
            case TZTIMESTAMP:
                return new TzTimestampSerDes(formats);
            case UTCDATEONLY:
                return new UtcDateOnly(formats);
            case UTCTIMEONLY:
                return new UtcTimeOnly(formats);
            case UTCTIMESTAMP:
                return new UtcTimestampSerDes(formats);
            default:
                return null;
        }
    }

    Class<T> getValueType();

    T parse(String string);

    String format(T value);
}
