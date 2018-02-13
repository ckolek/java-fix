package me.kolek.fix.serialization.metadata;

import me.kolek.fix.TagValue;
import me.kolek.fix.constants.FieldType;
import me.kolek.fix.serialization.Field;
import me.kolek.fix.serialization.field.FieldSerDes;
import me.kolek.fix.serialization.field.StringSerDes;
import me.kolek.util.io.WriterHelper;

import java.io.IOException;

/**
 * @author ckolek
 */
public class FieldMetadata<T> {
    private static final StringSerDes UDF_SER_DES = new StringSerDes();

    private final long id;
    private final int tagNum;
    private final String name;
    private final FieldSerDes<T> serDes;

    public FieldMetadata(long id, int tagNum, String name, FieldSerDes<T> serDes) {
        this.id = id;
        this.tagNum = tagNum;
        this.name = name;
        this.serDes = serDes;
    }

    public long getId() {
        return id;
    }

    public int getTagNum() {
        return tagNum;
    }

    public String getName() {
        return name;
    }

    public FieldType getType() {
        return serDes.getFieldType();
    }

    public FieldSerDes<T> getSerDes() {
        return serDes;
    }

    public Field<T> deserialize(TagValue tagValue) {
        return Field.raw(this, tagValue.getValue());
    }

    public Field<T> deserialize(String stringValue) {
        return Field.raw(this, stringValue);
    }

    public Field<T> toFieldValue(T value) {
        return Field.withValue(this, value);
    }

    public TagValue serialize(T value) {
        return new TagValue(tagNum, serDes.format(value));
    }

    WriterHelper toString(Field<T> field, WriterHelper writer) throws IOException {
        return writer.write(name).write('(').write(tagNum).write(") = ").write(field.getStringValue());
    }

    WriterHelper toString(T value, WriterHelper writer) throws IOException {
        return writer.write(name).write('(').write(tagNum).write(") = ").write(serDes.format(value));
    }

    public static FieldMetadata<String> udf(int tagNum) {
        return new FieldMetadata<>(tagNum, tagNum, "UDF#" + tagNum, UDF_SER_DES);
    }
}
