package me.kolek.fix.serialization.field;

import me.kolek.fix.constants.FieldType;

/**
 * @author ckolek
 */
public class CharSerDes extends FieldSerDesBase<Character> {
    public CharSerDes() {
        super(FieldType.CHAR, Character.class);
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
