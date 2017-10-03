package me.kolek.fix.serialization.field;

import java.time.OffsetTime;

/**
 * @author ckolek
 */
public class TzTimeOnlySerDes extends DateTimeSerDes<OffsetTime> {
    public TzTimeOnlySerDes(String... formats) {
        super(OffsetTime.class, OffsetTime::from, formats);
    }
}
