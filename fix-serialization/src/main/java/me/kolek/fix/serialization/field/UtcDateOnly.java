package me.kolek.fix.serialization.field;

import java.time.LocalDate;

/**
 * @author ckolek
 */
public class UtcDateOnly extends DateTimeSerDes<LocalDate> {
    public UtcDateOnly(String... formats) {
        super(LocalDate.class, LocalDate::from, formats);
    }
}
