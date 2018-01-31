package me.kolek.fix.constants;

import java.time.*;
import java.time.temporal.TemporalAccessor;

public enum FieldType {
    AMT(Double.class),
    BOOLEAN(Boolean.class),
    CHAR(Character.class),
    COUNTRY(String.class),
    CURRENCY(String.class),
    DATA(byte[].class),
    DATETIME(TemporalAccessor.class),
    DAYOFMONTH(Integer.class),
    EXCHANGE(String.class),
    FLOAT(Double.class),
    INT(Integer.class),
    LANGUAGE(String.class),
    LENGTH(Integer.class),
    LOCALMKTDATE(LocalDate.class),
    LOCALMKTTIME(LocalTime.class),
    MONTHYEAR(YearMonth.class),
    MULTIPLECHARVALUE(char[].class),
    MULTIPLESTRINGVALUE(String[].class),
    NUMINGROUP(Integer.class),
    PATTERN(String.class),
    PERCENTAGE(Double.class),
    PRICE(Double.class),
    PRICEOFFSET(Double.class),
    QTY(Double.class),
    RESERVED100PLUS(String.class),
    RESERVED1000PLUS(String.class),
    RESERVED4000PLUS(String.class),
    SEQNUM(Integer.class),
    STRING(String.class),
    TENOR(Period.class),
    TZTIMEONLY(OffsetTime.class),
    TZTIMESTAMP(OffsetDateTime.class),
    UTCDATEONLY(LocalDate.class),
    UTCTIMEONLY(OffsetTime.class),
    UTCTIMESTAMP(OffsetDateTime.class),
    XID(String.class),
    XIDREF(String.class),
    XMLDATA(String.class);

    private final Class<?> valueClass;

    FieldType(Class<?> valueClass) {
        this.valueClass = valueClass;
    }

    public Class<?> getValueClass() {
        return valueClass;
    }
}
