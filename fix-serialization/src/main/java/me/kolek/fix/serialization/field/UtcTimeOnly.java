package me.kolek.fix.serialization.field;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

/**
 * @author ckolek
 */
public class UtcTimeOnly extends DateTimeSerDes<OffsetTime> {
    public UtcTimeOnly(String... formats) {
        super(OffsetTime.class, temporal -> {
            LocalTime utcTime = LocalTime.from(temporal);
            return utcTime.atOffset(ZoneOffset.UTC);
        }, formats);
    }
}
