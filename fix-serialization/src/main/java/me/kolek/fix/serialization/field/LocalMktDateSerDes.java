package me.kolek.fix.serialization.field;

import me.kolek.fix.constants.FieldType;

import java.time.LocalDate;

/**
 * @author ckolek
 */
public class LocalMktDateSerDes extends DateTimeSerDes<LocalDate> {
    public LocalMktDateSerDes(String... formats) {
        super(FieldType.LOCALMKTDATE, LocalDate.class, LocalDate::from, formats);
    }
}
