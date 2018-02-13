package me.kolek.fix.serialization.field;

import me.kolek.fix.constants.FieldType;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

/**
 * @author ckolek
 */
public class UtcTimeOnlySerDes extends DateTimeSerDes<OffsetTime> {
    public UtcTimeOnlySerDes(String... formats) {
        super(FieldType.UTCTIMEONLY, OffsetTime.class, temporal -> {
            LocalTime utcTime = LocalTime.from(temporal);
            return utcTime.atOffset(ZoneOffset.UTC);
        }, formats);
    }
}
