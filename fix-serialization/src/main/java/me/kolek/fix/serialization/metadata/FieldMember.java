package me.kolek.fix.serialization.metadata;

import me.kolek.fix.TagValue;
import me.kolek.fix.serialization.Field;
import me.kolek.util.io.WriterHelper;

import java.io.IOException;
import java.util.List;

/**
 * @author ckolek
 */
public class FieldMember<T> extends StructureMember {
    private final FieldMetadata<T> metadata;

    public FieldMember(FieldMetadata<T> metadata, int orderNumber, boolean trailing, boolean required) {
        super(orderNumber, trailing, required);
        this.metadata = metadata;
    }

    public FieldMetadata<T> getMetadata() {
        return metadata;
    }

    @Override
    void serialize(AbstractStructure<?> structure, List<TagValue> tagValues) {
        Field value = structure.get(metadata.getTagNum());
        if (value != null) {
            tagValues.add(value.toTagValue());
        }
    }

    @Override
    void getAllFieldValues(AbstractStructure<?> structure, List<Field<?>> fields) {
        Field value = structure.get(metadata.getTagNum());
        if (value != null) {
            fields.add(value);
        }
    }

    @Override
    WriterHelper toString(AbstractStructure<?> structure, WriterHelper writer) throws IOException {
        Field<T> value = (Field<T>) structure.get(metadata.getTagNum());
        if (value != null) {
            return metadata.toString(value, writer).newLine();
        }
        return writer;
    }
}
