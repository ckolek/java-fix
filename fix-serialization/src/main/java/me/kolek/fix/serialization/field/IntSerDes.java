package me.kolek.fix.serialization.field;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * @author ckolek
 */
public class IntSerDes extends NumberSerDes<Integer> {
    public IntSerDes(Double multiplier, String... formats) {
        super(Integer.class, multiplier, formats);
    }

    @Override
    protected Integer parseValue(String string) throws NumberFormatException {
        return Integer.valueOf(string);
    }

    @Override
    protected Integer parseValue(String string, NumberFormat format) throws ParseException {
        return format.parse(string).intValue();
    }

    @Override
    protected String formatValue(Integer value) {
        return value.toString();
    }

    @Override
    protected String formatValue(Integer value, NumberFormat format) throws IllegalArgumentException {
        return format.format(value);
    }

    @Override
    protected Integer multiply(Integer value, double multiplier) {
        return (int) (value * multiplier);
    }
}
