package me.kolek.fix.serialization.field;

import me.kolek.fix.constants.FieldType;

import java.time.LocalDate;

/**
 * @author ckolek
 */
public class UtcDateOnly extends DateTimeSerDes<LocalDate> {
    public UtcDateOnly(String... formats) {
        super(FieldType.UTCDATEONLY, LocalDate.class, LocalDate::from, formats);
    }
}
