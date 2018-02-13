package me.kolek.fix.serialization.metadata;

import me.kolek.fix.TagValue;
import me.kolek.fix.serialization.Group;
import me.kolek.util.io.WriterHelper;

import java.io.IOException;
import java.util.List;

/**
 * @author ckolek
 */
public class GroupMember extends StructureMember {
    private final GroupMetadata metadata;

    public GroupMember(GroupMetadata metadata, int orderNumber, boolean isTrailing, boolean required) {
        super(orderNumber, isTrailing, required);
        this.metadata = metadata;
    }

    public GroupMetadata getMetadata() {
        return metadata;
    }

    @Override
    void serialize(AbstractStructure<?> structure, List<TagValue> tagValues) {
        Group group = structure.getGroup(metadata.getNumInGroupField().getTagNum());
        if (group != null) {
            metadata.serialize(group, tagValues);
        }
    }

    @Override
    void getAllFieldValues(AbstractStructure<?> structure, List<FieldValue<?>> fieldValues) {
        Group group = structure.getGroup(metadata.getNumInGroupField().getTagNum());
        if (group != null) {
            metadata.getAllFieldValues(group, fieldValues);
        }
    }

    @Override
    WriterHelper toString(AbstractStructure<?> structure, WriterHelper writer) throws IOException {
        Group group = structure.getGroup(metadata.getNumInGroupField().getTagNum());
        if (group != null && !group.isEmpty()) {
            writer.write(metadata.getName()).write(':').newLine();
            return metadata.toString(group, writer.addIndent()).removeIndent();
        }
        return writer;
    }
}
