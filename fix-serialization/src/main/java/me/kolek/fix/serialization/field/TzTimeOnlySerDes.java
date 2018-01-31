package me.kolek.fix.serialization.field;

import me.kolek.fix.constants.FieldType;

import java.time.OffsetTime;

/**
 * @author ckolek
 */
public class TzTimeOnlySerDes extends DateTimeSerDes<OffsetTime> {
    public TzTimeOnlySerDes(String... formats) {
        super(FieldType.TZTIMEONLY, OffsetTime.class, OffsetTime::from, formats);
    }
}
