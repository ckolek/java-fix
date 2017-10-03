package me.kolek.fix.serialization.field;

import java.time.OffsetDateTime;

/**
 * @author ckolek
 */
public class TzTimestampSerDes extends DateTimeSerDes<OffsetDateTime> {
    public TzTimestampSerDes(String... formats) {
        super(OffsetDateTime.class, OffsetDateTime::from, formats);
    }
}
