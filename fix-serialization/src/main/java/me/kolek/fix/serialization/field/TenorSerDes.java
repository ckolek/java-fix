package me.kolek.fix.serialization.field;

import me.kolek.fix.constants.FieldType;

import java.time.Period;

public class TenorSerDes extends FieldSerDesBase<Period> {
    protected TenorSerDes() {
        super(FieldType.TENOR, Period.class);
    }

    @Override
    protected Period parseNonNull(String string) {
        int magnitude = Integer.parseInt(string.substring(1));
        switch (string.charAt(0)) {
            case 'Y':
                return Period.ofYears(magnitude);
            case 'M':
                return Period.ofMonths(magnitude);
            case 'W':
                return Period.ofWeeks(magnitude);
            case 'D':
                return Period.ofDays(magnitude);
            default:
                return Period.ZERO;
        }
    }

    @Override
    protected String formatNonNull(Period value) {
        if (value.getYears() != 0) {
            return value.getYears() + "Y";
        } else if (value.getMonths() != 0) {
            return value.getMonths() + "M";
        } else if (value.getDays() % 7 == 0) {
            return (value.getDays() / 7) + "W";
        } else {
            return value.getDays() + "D";
        }
    }
}
