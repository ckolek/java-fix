package me.kolek.fix.serialization.metadata;

import gnu.trove.impl.unmodifiable.TUnmodifiableIntObjectMap;
import gnu.trove.impl.unmodifiable.TUnmodifiableIntSet;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import me.kolek.fix.TagValue;
import me.kolek.fix.serialization.Component;
import me.kolek.fix.serialization.Group;
import me.kolek.fix.serialization.field.StringSerDes;
import me.kolek.util.exception.CannotHappenException;
import me.kolek.util.io.WriterHelper;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * @author ckolek
 */
public abstract class StructureMetadata<S extends MetadataStructure<?>> {
    private final long id;
    private final String name;
    private final List<StructureMember> members;
    private final Map<String, ComponentMember> componentsByName;
    private final TIntObjectMap<ComponentMember> componentsByContainedTagNum;
    private final Map<String, GroupMember> groupsByName;
    private final TIntObjectMap<GroupMember> groupsByNumInGroupTagNum;
    private final Map<String, FieldMember> fieldsByName;
    private final TIntObjectMap<FieldMember> fieldsByTagNum;
    private final TIntSet containedTagNums;

    public StructureMetadata(long id, String name, List<StructureMember> members) {
        this.id = id;
        this.name = name;
        this.members = Collections.unmodifiableList(members);

        Map<String, ComponentMember> componentsByName = new HashMap<>();
        TIntObjectMap<ComponentMember> componentsByContainedTagNum = new TIntObjectHashMap<>();
        Map<String, GroupMember> groupsByName = new HashMap<>();
        TIntObjectMap<GroupMember> groupsByNumInGroupTagNum = new TIntObjectHashMap<>();
        Map<String, FieldMember> fieldsByName = new HashMap<>();
        TIntObjectMap<FieldMember> fieldsByTagNum = new TIntObjectHashMap<>();
        TIntSet containedTagNums = new TIntHashSet();

        members.forEach(member -> {
            if (member instanceof ComponentMember) {
                ComponentMember _member = (ComponentMember) member;
                componentsByName.put(_member.getMetadata().getName(), _member);
                ((StructureMetadata) _member.getMetadata()).getContainedTagNums().forEach(tagNum -> {
                    componentsByContainedTagNum.put(tagNum, _member);
                    containedTagNums.add(tagNum);
                    return true;
                });
            } else if (member instanceof GroupMember) {
                GroupMember _member = (GroupMember) member;
                groupsByName.put(_member.getMetadata().getName(), _member);
                groupsByNumInGroupTagNum.put(_member.getMetadata().getNumInGroupField().getTagNum(), _member);
                containedTagNums.add(_member.getMetadata().getNumInGroupField().getTagNum());
            } else if (member instanceof FieldMember) {
                FieldMember _member = (FieldMember) member;
                fieldsByName.put(_member.getMetadata().getName(), _member);
                fieldsByTagNum.put(_member.getMetadata().getTagNum(), _member);
                containedTagNums.add(_member.getMetadata().getTagNum());
            }
        });

        this.componentsByName = Collections.unmodifiableMap(componentsByName);
        this.componentsByContainedTagNum = new TUnmodifiableIntObjectMap<>(componentsByContainedTagNum);
        this.groupsByName = Collections.unmodifiableMap(groupsByName);
        this.groupsByNumInGroupTagNum = new TUnmodifiableIntObjectMap<>(groupsByNumInGroupTagNum);
        this.fieldsByName = Collections.unmodifiableMap(fieldsByName);
        this.fieldsByTagNum = new TUnmodifiableIntObjectMap<>(fieldsByTagNum);
        this.containedTagNums = new TUnmodifiableIntSet(containedTagNums);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StructureMember> getMembers() {
        return members;
    }

    public boolean hasComponentMember(String name) {
        return componentsByName.containsKey(name);
    }

    public ComponentMember getComponentMember(String name) {
        return componentsByName.get(name);
    }

    public boolean hasComponentContaining(int tagNum) {
        return componentsByContainedTagNum.containsKey(tagNum);
    }

    public ComponentMember getComponentMemberContaining(int tagNum) {
        return componentsByContainedTagNum.get(tagNum);
    }

    public boolean hasGroupMember(String name) {
        return groupsByName.containsKey(name);
    }

    public GroupMember getGroupMember(String name) {
        return groupsByName.get(name);
    }

    public boolean hasGroupMember(int numInGroupTagNum) {
        return groupsByNumInGroupTagNum.containsKey(numInGroupTagNum);
    }

    public GroupMember getGroupMember(int numInGroupTagNum) {
        return groupsByNumInGroupTagNum.get(numInGroupTagNum);
    }

    public boolean hasFieldMember(String name) {
        return fieldsByName.containsKey(name);
    }

    public FieldMember getFieldMember(String name) {
        return fieldsByName.get(name);
    }

    public boolean hasFieldMember(int tagNum) {
        return fieldsByTagNum.containsKey(tagNum);
    }

    public FieldMember getFieldMember(int tagNum) {
        return fieldsByTagNum.get(tagNum);
    }

    private TIntSet getContainedTagNums() {
        return containedTagNums;
    }

    public abstract S newStructure();

    public S deserialize(List<TagValue> tagValues) {
        return deserialize(tagValues.listIterator());
    }

    public S deserialize(ListIterator<TagValue> tagValues) {
        S structure = newStructure();

        deserialize(structure, tagValues);

        return structure;
    }

    void deserialize(S structure, ListIterator<TagValue> tagValues) {
        while (tagValues.hasNext()) {
            TagValue tagValue = tagValues.next();
            if (!deserialize(structure, tagValue, tagValues)) {
                structure.addUdf(FieldValue.raw(tagValue.getTagNum(), new StringSerDes(), tagValue.getValue()));
            }
        }
    }

    boolean deserialize(S structure, TagValue tagValue, ListIterator<TagValue> tagValues) {
        FieldMember fieldMember = fieldsByTagNum.get(tagValue.getTagNum());
        if (fieldMember != null) {
            FieldMetadata fieldMetadata = fieldMember.getMetadata();
            FieldValue<?> value = fieldMetadata.deserialize(tagValue);
            structure.set(value);
            return true;
        }
        GroupMember groupMember = groupsByNumInGroupTagNum.get(tagValue.getTagNum());
        if (groupMember != null) {
            GroupMetadata groupMetadata = groupMember.getMetadata();
            Group group = structure.getOrCreateGroup(groupMetadata.getNumInGroupField().getTagNum());
            groupMetadata.deserialize(group, tagValue, tagValues);
            structure.set(group);
            return true;
        }
        ComponentMember componentMember = componentsByContainedTagNum.get(tagValue.getTagNum());
        if (componentMember != null) {
            ComponentMetadata componentMetadata = componentMember.getMetadata();
            Component component = structure.getOrCreateComponent(componentMetadata.getName());
            if (componentMetadata.deserialize(component, tagValue, tagValues)) {
                return true;
            }
        }
        return false;
    }

    List<TagValue> serialize(S structure) {
        List<TagValue> tagValues = new ArrayList<>();
        serialize(structure, tagValues);
        return tagValues;
    }

    void serialize(S structure, List<TagValue> tagValues) {
        List<StructureMember> members = getPresentMembers(structure);
        List<StructureMember> trailingMembers = new ArrayList<>();

        members.forEach(member -> {
            if (member.isTrailing()) {
                trailingMembers.add(member);
            } else {
                member.serialize(structure, tagValues);
            }
        });

        structure.getUdfs().forEach(udf -> tagValues.add(udf.toTagValue()));

        trailingMembers.forEach(member -> member.serialize(structure, tagValues));
    }

    List<FieldValue<?>> getAllFieldValues(S structure) {
        List<FieldValue<?>> fieldValues = new ArrayList<>();
        getAllFieldValues(structure, fieldValues);
        return fieldValues;
    }

    void getAllFieldValues(S structure, List<FieldValue<?>> fieldValues) {

    }

    String toString(S structure) {
        WriterHelper writer = new WriterHelper(new StringWriter());
        try {
            return toString(structure, writer.write(name).write(':').newLine().addIndent()).toString();
        } catch (IOException e) {
            throw new CannotHappenException(e);
        }
    }

    WriterHelper toString(S structure, WriterHelper writer) throws IOException {
        List<StructureMember> members = getPresentMembers(structure);
        List<StructureMember> trailingMembers = new ArrayList<>();

        for (StructureMember member : members) {
            if (member.isTrailing()) {
                trailingMembers.add(member);
            } else {
                member.toString(structure, writer);
            }
        }

        for (FieldValue<?> udf : structure.getUdfs()) {
            writer.write("UDF(").write(udf.getTagNum()).write(") = ").write(udf.getStringValue()).newLine();
        }

        for (StructureMember member : trailingMembers) {
            if (member.isTrailing()) {
                trailingMembers.add(member);
            } else {
                member.toString(structure, writer);
            }
        }

        return writer;
    }

    private List<StructureMember> getPresentMembers(S structure) {
        Collection<Component> components = structure.getComponents();
        Collection<Group> groups = structure.getGroups();
        Collection<FieldValue<?>> fieldValues = structure.getFieldValues();

        List<StructureMember> members = new ArrayList<>(components.size() + groups.size() + fieldValues.size());
        components.forEach(component -> members.add(getComponentMember(component.getMetadata().getName())));
        groups.forEach(group -> members.add(getGroupMember(group.getMetadata().getNumInGroupField().getTagNum())));
        fieldValues.forEach(fieldValue -> members.add(getFieldMember(fieldValue.getTagNum())));
        members.sort(Comparator.comparingInt(StructureMember::getOrderNumber));

        return members;
    }
}
