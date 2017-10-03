package me.kolek.fix.serialization.field;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * @author ckolek
 */
public class FloatSerDes extends NumberSerDes<Double> {
    public FloatSerDes(Double multiplier, String... formats) {
        super(Double.class, multiplier, formats);
    }

    @Override
    protected Double parseValue(String string) {
        return Double.valueOf(string);
    }

    @Override
    protected Double parseValue(String string, NumberFormat format) throws ParseException {
        return format.parse(string).doubleValue();
    }

    @Override
    protected String formatValue(Double value) {
        return value.toString();
    }

    @Override
    protected String formatValue(Double value, NumberFormat format) {
        return format.format(value);
    }

    @Override
    protected Double multiply(Double value, double multiplier) {
        return value * multiplier;
    }
}
