package me.kolek.fix.serialization.field;

/**
 * @author ckolek
 */
public class CharSerDes extends FieldSerDesBase<Character> {
    public CharSerDes() {
        super(Character.class);
    }

    @Override
    protected Character parseNonNull(String string) {
        return string.charAt(0);
    }

    @Override
    protected String formatNonNull(Character value) {
        return value.toString();
    }
}
