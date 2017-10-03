package me.kolek.fix.serialization.field;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * @author ckolek
 */
public class UtcTimestampSerDes extends DateTimeSerDes<OffsetDateTime> {
    public UtcTimestampSerDes(String... formats) {
        super(OffsetDateTime.class, temporal -> {
            LocalDateTime utcDateTime = LocalDateTime.from(temporal);
            return utcDateTime.atOffset(ZoneOffset.UTC);
        }, formats);
    }
}
