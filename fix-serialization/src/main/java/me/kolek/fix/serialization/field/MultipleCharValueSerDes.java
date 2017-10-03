package me.kolek.fix.serialization.field;

/**
 * @author ckolek
 */
public class MultipleCharValueSerDes extends FieldSerDesBase<char[]> {
    public MultipleCharValueSerDes() {
        super(char[].class);
    }

    @Override
    protected char[] parseNonNull(String string) {
        String[] parts = string.split(" ");
        char[] value = new char[parts.length];

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].length() != 1) {
                // TODO
            }
            value[i] = parts[i].charAt(0);
        }

        return value;
    }

    @Override
    protected String formatNonNull(char[] value) {
        StringBuilder string = new StringBuilder();

        for (int i = 0; i < value.length; i++) {
            string.append(value[i]);
            if (i < value.length - 1) {
                string.append(' ');
            }
        }

        return string.toString();
    }
}
