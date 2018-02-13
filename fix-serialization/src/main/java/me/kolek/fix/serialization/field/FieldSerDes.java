package me.kolek.fix.serialization.field;

import me.kolek.fix.constants.FieldType;

import java.time.temporal.TemporalAccessor;

/**
 * @author ckolek
 */
public interface FieldSerDes<T> {
    static FieldSerDes<?> getInstance(FieldType fieldType, String[] formats, Double multiplier) {
        switch (fieldType) {
            case AMT:
                return new FloatSerDes(FieldType.AMT, multiplier, formats);
            case BOOLEAN:
                return new BooleanSerDes();
            case CHAR:
                return new CharSerDes();
            case COUNTRY:
                return new StringSerDes(FieldType.COUNTRY);
            case DAYOFMONTH:
                return new IntSerDes(FieldType.DAYOFMONTH, multiplier, formats);
            case CURRENCY:
                return new StringSerDes(FieldType.CURRENCY);
            case DATA:
                return new DataSerDes();
            case DATETIME:
                return new DateTimeSerDes<>(FieldType.DATETIME, TemporalAccessor.class, t -> t, formats);
            case EXCHANGE:
                return new StringSerDes(FieldType.EXCHANGE);
            case FLOAT:
                return new FloatSerDes(multiplier, formats);
            case INT:
                return new IntSerDes(multiplier, formats);
            case LANGUAGE:
                return new StringSerDes(FieldType.LANGUAGE);
            case LENGTH:
                return new IntSerDes(FieldType.LENGTH, multiplier, formats);
            case LOCALMKTDATE:
                return new LocalMktDateSerDes(formats);
            case LOCALMKTTIME:
                return new LocalMktTimeSerDes(formats);
            case MONTHYEAR:
                return new MonthYearSerDes(formats);
            case MULTIPLECHARVALUE:
                return new MultipleCharValueSerDes();
            case MULTIPLESTRINGVALUE:
                return new MultipleStringValueSerDes();
            case NUMINGROUP:
                return new IntSerDes(FieldType.NUMINGROUP, multiplier, formats);
            case PATTERN:
                return new StringSerDes(FieldType.PATTERN);
            case PERCENTAGE:
                return new FloatSerDes(multiplier, formats);
            case PRICE:
                return new FloatSerDes(FieldType.PRICE, multiplier, formats);
            case PRICEOFFSET:
                return new FloatSerDes(FieldType.PRICEOFFSET, multiplier, formats);
            case QTY:
                return new FloatSerDes(FieldType.QTY, multiplier, formats);
            case RESERVED100PLUS:
            case RESERVED1000PLUS:
            case RESERVED4000PLUS:
                return new StringSerDes(fieldType);
            case SEQNUM:
                return new IntSerDes(FieldType.SEQNUM, multiplier, formats);
            case STRING:
                return new StringSerDes();
            case TENOR:
                return new TenorSerDes();
            case TZTIMEONLY:
                return new TzTimeOnlySerDes(formats);
            case TZTIMESTAMP:
                return new TzTimestampSerDes(formats);
            case UTCDATEONLY:
                return new UtcDateOnlySerDes(formats);
            case UTCTIMEONLY:
                return new UtcTimeOnlySerDes(formats);
            case UTCTIMESTAMP:
                return new UtcTimestampSerDes(formats);
            case XID:
                return new StringSerDes(FieldType.XID);
            case XIDREF:
                return new StringSerDes(FieldType.XIDREF);
            case XMLDATA:
                return new StringSerDes(FieldType.XMLDATA);
            default:
                return null;
        }
    }

    FieldType getFieldType();

    Class<T> getValueType();

    T parse(String string);

    String format(T value);
}
