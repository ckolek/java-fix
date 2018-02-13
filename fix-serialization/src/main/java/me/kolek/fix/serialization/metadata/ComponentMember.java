package me.kolek.fix.serialization.metadata;

import me.kolek.fix.TagValue;
import me.kolek.fix.serialization.Component;
import me.kolek.fix.serialization.Field;
import me.kolek.util.io.WriterHelper;

import java.io.IOException;
import java.util.List;

/**
 * @author ckolek
 */
public class ComponentMember extends StructureMember {
    private final ComponentMetadata metadata;

    public ComponentMember(ComponentMetadata metadata, int orderNumber, boolean trailing, boolean required) {
        super(orderNumber, trailing, required);
        this.metadata = metadata;
    }

    public ComponentMetadata getMetadata() {
        return metadata;
    }

    @Override
    void serialize(AbstractStructure<?> structure, List<TagValue> tagValues) {
        Component component = structure.getComponent(metadata.getName());
        if (component != null) {
            metadata.serialize(component, tagValues);
        }
    }

    @Override
    void getAllFieldValues(AbstractStructure<?> structure, List<Field<?>> fields) {
        Component component = structure.getComponent(metadata.getName());
        if (component != null) {
            metadata.getAllFieldValues(component, fields);
        }
    }

    @Override
    WriterHelper toString(AbstractStructure<?> structure, WriterHelper writer) throws IOException {
        if (!structure.isEmpty()) {
            Component component = structure.getComponent(metadata.getName());
            if (component != null) {
                writer.write(metadata.getName()).write(':').newLine();
                return metadata.toString(component, writer.addIndent()).removeIndent();
            }
        }
        return writer;
    }
}
