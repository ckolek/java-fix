package me.kolek.fix.serialization.field;

import java.time.LocalDate;

/**
 * @author ckolek
 */
public class LocalMktDateSerDes extends DateTimeSerDes<LocalDate> {
    public LocalMktDateSerDes(String... formats) {
        super(LocalDate.class, LocalDate::from, formats);
    }
}
