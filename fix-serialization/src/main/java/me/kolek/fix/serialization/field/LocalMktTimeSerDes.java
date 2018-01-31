package me.kolek.fix.serialization.field;

import me.kolek.fix.constants.FieldType;

import java.time.LocalTime;

/**
 * @author ckolek
 */
public class LocalMktTimeSerDes extends DateTimeSerDes<LocalTime> {
    public LocalMktTimeSerDes(String... formats) {
        super(FieldType.LOCALMKTTIME, LocalTime.class, LocalTime::from, formats);
    }
}
