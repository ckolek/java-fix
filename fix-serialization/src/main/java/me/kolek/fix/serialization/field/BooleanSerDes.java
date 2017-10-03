package me.kolek.fix.serialization.field;

/**
 * @author ckolek
 */
public class BooleanSerDes extends FieldSerDesBase<Boolean> {
    public BooleanSerDes() {
        super(Boolean.class);
    }

    @Override
    protected Boolean parseNonNull(String string) {
        return "Y".equalsIgnoreCase(string);
    }

    @Override
    protected String formatNonNull(Boolean value) {
        return value ? "Y" : "N";
    }
}
