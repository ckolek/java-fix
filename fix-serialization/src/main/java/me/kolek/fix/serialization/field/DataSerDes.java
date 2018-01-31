package me.kolek.fix.serialization.field;

import me.kolek.fix.constants.FieldType;

/**
 * @author ckolek
 */
public class DataSerDes extends FieldSerDesBase<byte[]> {
    public DataSerDes() {
        super(FieldType.DATA, byte[].class);
    }

    @Override
    protected byte[] parseNonNull(String string) {
        return string.getBytes();
    }

    @Override
    protected String formatNonNull(byte[] value) {
        return new String(value);
    }
}
