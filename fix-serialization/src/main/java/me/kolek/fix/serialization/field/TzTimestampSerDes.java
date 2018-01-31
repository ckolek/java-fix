package me.kolek.fix.serialization.field;

import me.kolek.fix.constants.FieldType;

import java.time.OffsetDateTime;

/**
 * @author ckolek
 */
public class TzTimestampSerDes extends DateTimeSerDes<OffsetDateTime> {
    public TzTimestampSerDes(String... formats) {
        super(FieldType.TZTIMESTAMP, OffsetDateTime.class, OffsetDateTime::from, formats);
    }
}
