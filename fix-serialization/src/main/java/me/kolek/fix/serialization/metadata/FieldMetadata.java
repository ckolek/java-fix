package me.kolek.fix.serialization.metadata;

import me.kolek.fix.TagValue;
import me.kolek.fix.serialization.field.FieldSerDes;
import me.kolek.util.WriterHelper;

import java.io.IOException;

/**
 * @author ckolek
 */
public class FieldMetadata<T> {
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

    public FieldSerDes<T> getSerDes() {
        return serDes;
    }

    public FieldValue<T> deserialize(TagValue tagValue) {
        return FieldValue.raw(tagNum, serDes, tagValue.getValue());
    }

    public FieldValue<T> deserialize(String stringValue) {
        return FieldValue.raw(tagNum, serDes, stringValue);
    }

    public FieldValue<T> toFieldValue(T value) {
        return FieldValue.withValue(tagNum, serDes, value);
    }

    public TagValue serialize(T value) {
        return new TagValue(tagNum, serDes.format(value));
    }

    WriterHelper toString(FieldValue<T> fieldValue, WriterHelper writer) throws IOException {
        return writer.write(name).write('(').write(tagNum).write(") = ").write(fieldValue.getStringValue());
    }

    WriterHelper toString(T value, WriterHelper writer) throws IOException {
        return writer.write(name).write('(').write(tagNum).write(") = ").write(serDes.format(value));
    }
}
