package me.kolek.fix.serialization.field;

/**
 * @author ckolek
 */
public class StringSerDes extends FieldSerDesBase<String> {
    public StringSerDes() {
        super(String.class);
    }

    @Override
    protected String parseNonNull(String string) {
        return string;
    }

    @Override
    protected String formatNonNull(String value) {
        return value;
    }
}
