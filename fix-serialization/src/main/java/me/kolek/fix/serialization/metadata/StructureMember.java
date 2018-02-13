package me.kolek.fix.serialization.metadata;

import me.kolek.fix.TagValue;
import me.kolek.util.io.WriterHelper;

import java.io.IOException;
import java.util.List;

/**
 * @author ckolek
 */
public abstract class StructureMember {
    private final int orderNumber;
    private final boolean trailing;
    private final boolean required;

    public StructureMember(int orderNumber, boolean trailing, boolean required) {
        this.orderNumber = orderNumber;
        this.trailing = trailing;
        this.required = required;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public boolean isTrailing() {
        return trailing;
    }

    public boolean isRequired() {
        return required;
    }

    abstract void serialize(AbstractStructure<?> structure, List<TagValue> tagValues);

    abstract void getAllFieldValues(AbstractStructure<?> structure, List<FieldValue<?>> fieldValues);

    abstract WriterHelper toString(AbstractStructure<?> structure, WriterHelper writer) throws IOException;
}
