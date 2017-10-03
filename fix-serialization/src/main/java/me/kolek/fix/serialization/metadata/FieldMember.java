package me.kolek.fix.serialization.metadata;

import me.kolek.fix.TagValue;
import me.kolek.util.WriterHelper;

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
    void serialize(MetadataStructure<?> structure, List<TagValue> tagValues) {
        FieldValue value = structure.get(metadata.getTagNum());
        if (value != null) {
            tagValues.add(value.toTagValue());
        }
    }

    @Override
    void getAllFieldValues(MetadataStructure<?> structure, List<FieldValue<?>> fieldValues) {
        FieldValue value = structure.get(metadata.getTagNum());
        if (value != null) {
            fieldValues.add(value);
        }
    }

    @Override
    WriterHelper toString(MetadataStructure<?> structure, WriterHelper writer) throws IOException {
        FieldValue<T> value = (FieldValue<T>) structure.get(metadata.getTagNum());
        if (value != null) {
            return metadata.toString(value, writer).newLine();
        }
        return writer;
    }
}
