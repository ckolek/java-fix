package me.kolek.fix.serialization.field;

import me.kolek.fix.constants.FieldType;

import java.time.LocalDate;

/**
 * @author ckolek
 */
public class UtcDateOnlySerDes extends DateTimeSerDes<LocalDate> {
    public UtcDateOnlySerDes(String... formats) {
        super(FieldType.UTCDATEONLY, LocalDate.class, LocalDate::from, formats);
    }
}
