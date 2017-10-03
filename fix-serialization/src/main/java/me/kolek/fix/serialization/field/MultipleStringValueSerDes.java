package me.kolek.fix.serialization.field;

import java.util.Arrays;

/**
 * @author ckolek
 */
public class MultipleStringValueSerDes extends FieldSerDesBase<String[]> {
    public MultipleStringValueSerDes() {
        super(String[].class);
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
