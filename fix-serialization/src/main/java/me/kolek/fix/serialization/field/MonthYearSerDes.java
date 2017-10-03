package me.kolek.fix.serialization.field;

import java.time.YearMonth;

/**
 * @author ckolek
 */
public class MonthYearSerDes extends DateTimeSerDes<YearMonth> {
    public MonthYearSerDes(String... formats) {
        super(YearMonth.class, YearMonth::from, formats);
    }
}
