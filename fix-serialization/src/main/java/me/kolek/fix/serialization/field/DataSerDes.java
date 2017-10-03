package me.kolek.fix.serialization.field;

/**
 * @author ckolek
 */
public class DataSerDes extends FieldSerDesBase<byte[]> {
    public DataSerDes() {
        super(byte[].class);
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
