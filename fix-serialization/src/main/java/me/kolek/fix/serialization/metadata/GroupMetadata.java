package me.kolek.fix.serialization.metadata;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import me.kolek.fix.TagValue;
import me.kolek.fix.serialization.Group;
import me.kolek.util.io.WriterHelper;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author ckolek
 */
public class GroupMetadata extends StructureMetadata<Group.Element> {
    private final FieldMetadata<Integer> numInGroupFieldMetadata;

    public GroupMetadata(long id, String name, FieldMetadata<Integer> numInGroupFieldMetadata,
            List<StructureMember> members) {
        super(id, name, members);
        this.numInGroupFieldMetadata = numInGroupFieldMetadata;
    }

    public FieldMetadata<Integer> getNumInGroupField() {
        return numInGroupFieldMetadata;
    }

    @Override
    public Group.Element newStructure() {
        return new Group(this, null).newElement();
    }

    void deserialize(Group group, TagValue tagValue, ListIterator<TagValue> tagValues) {
        final int startTag = tagValue.getTagNum();
        final int startIndex = tagValues.previousIndex();

        int numElements = numInGroupFieldMetadata.deserialize(tagValue).getValue();
        if (numElements > 0) {
            Group.Element element = group.newElement();

            TIntSet encounteredTags = new TIntHashSet();

            while (tagValues.hasNext()) {
                tagValue = tagValues.next();
                if (!encounteredTags.contains(tagValue.getTagNum())) {
                    encounteredTags.add(tagValue.getTagNum());
                    if (!deserialize(element, tagValue, tagValues)) {
                        tagValues.previous();
                        break;
                    }
                } else if (group.size() < numElements) {
                    // the current tag doesn't belong to this element. go back so next element can handle it
                    tagValues.previous();
                    element = group.newElement();
                    encounteredTags.clear();
                } else {
                    break;
                }
            }
        }

        if (group.size() != numElements) {
//            throw FixDeserializationException.incorrectNumInGroup(startTag, startIndex, numElements, group.size());
        }
    }

    void serialize(Group group, List<TagValue> tagValues) {
        if (group.isEmpty()) {
            return;
        }

        tagValues.add(numInGroupFieldMetadata.serialize(group.size()));

        for (Group.Element element : group) {
            serialize(element, tagValues);
        }
    }

    void getAllFieldValues(Group group, List<FieldValue<?>> fieldValues) {
        if (group.isEmpty()) {
            return;
        }

        fieldValues.add(group.getNumInGroup());

        for (Group.Element element : group) {
            getAllFieldValues(element, fieldValues);
        }
    }

    WriterHelper toString(Group group, WriterHelper writer) throws IOException {
        numInGroupFieldMetadata.toString(group.size(), writer).newLine();

        for (Iterator<Group.Element> iter = group.iterator(); iter.hasNext(); ) {
            Group.Element element = iter.next();
            toString(element, writer.addIndent()).removeIndent();
            if (iter.hasNext()) {
                writer.write("  ----------").newLine();
            }
        }

        return writer;
    }
}
