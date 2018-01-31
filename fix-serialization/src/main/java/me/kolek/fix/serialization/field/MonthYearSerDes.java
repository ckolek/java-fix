package me.kolek.fix.serialization.field;

import me.kolek.fix.constants.FieldType;

import java.time.YearMonth;

/**
 * @author ckolek
 */
public class MonthYearSerDes extends DateTimeSerDes<YearMonth> {
    public MonthYearSerDes(String... formats) {
        super(FieldType.MONTHYEAR, YearMonth.class, YearMonth::from, formats);
    }
}
