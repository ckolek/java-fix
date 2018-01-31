package me.kolek.fix.serialization.field;

import me.kolek.fix.constants.FieldType;

import java.util.Arrays;

/**
 * @author ckolek
 */
public class MultipleStringValueSerDes extends FieldSerDesBase<String[]> {
    public MultipleStringValueSerDes() {
        super(FieldType.MULTIPLESTRINGVALUE, String[].class);
    }

    @Override
    protected String[] parseNonNull(String string) {
        return string.split(" ");
    }

    @Override
    protected String formatNonNull(String[] value) {
        return String.join(" ", Arrays.asList(value));
    }
}
