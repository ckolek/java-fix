package me.kolek.fix.serialization;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import me.kolek.fix.serialization.metadata.*;

import java.util.*;

/**
 * @author ckolek
 */
public abstract class Structure<MD extends StructureMetadata> extends
        AbstractStructure<MD> {
    private final Structure<?> parent;

    private final Map<String, Component> components;
    private final TIntObjectMap<Group> groups;
    private final TIntObjectMap<Field<?>> fields;
    private final List<Field<?>> udfs;

    protected Structure(MD metadata, Structure<?> parent) {
        super(metadata);
        this.parent = parent;

        this.components = new HashMap<>();
        this.groups = new TIntObjectHashMap<>();
        this.fields = new TIntObjectHashMap<>();
        this.udfs = new ArrayList<>();
    }

    public Structure<?> getParent() {
        return parent;
    }

    @Override
    public Component getComponent(String name) {
        return components.get(name);
    }

    @Override
    public Collection<Component> getComponents() {
        return components.values();
    }

    @Override
    public Component getOrCreateComponent(String name) {
        Component component = getComponent(name);
        if (component == null) {
            ComponentMember member = metadata.getComponentMember(name);
            if (member == null) {
                throw new IllegalArgumentException(
                        "structure " + metadata.getName() + " does not contain component named \"" + name + "\"");
            }
            set(component = new Component(member.getMetadata(), this));
        }
        return component;
    }

    @Override
    public void set(Component component) {
        components.put(component.getMetadata().getName(), component);
    }

    @Override
    public Group getGroup(int numInGroupTagNum) {
        return groups.get(numInGroupTagNum);
    }

    @Override
    public Group getGroup(String name) {
        GroupMember member = metadata.getGroupMember(name);
        return (member != null) ? groups.get(member.getMetadata().getNumInGroupField().getTagNum()) : null;
    }

    @Override
    public Collection<Group> getGroups() {
        return groups.valueCollection();
    }

    @Override
    public Group getOrCreateGroup(int numInGroupTagNum) {
        Group group = getGroup(numInGroupTagNum);
        if (group == null) {
            GroupMember member = metadata.getGroupMember(numInGroupTagNum);
            if (member == null) {
                throw new IllegalArgumentException(
                        "structure " + metadata.getName() + " does not contain group with NumInGroup TagNum " +
                                numInGroupTagNum);
            }
            set(group = new Group(member.getMetadata(), this));
        }
        return group;
    }

    @Override
    public Group getOrCreateGroup(String name) {
        Group group = getGroup(name);
        if (group == null) {
            GroupMember member = metadata.getGroupMember(name);
            if (member == null) {
                throw new IllegalArgumentException(
                        "structure " + metadata.getName() + " does not contain group named " + name);
            }
            set(group = new Group(member.getMetadata(), this));
        }
        return group;
    }

    @Override
    public void set(Group group) {
        groups.put(group.getMetadata().getNumInGroupField().getTagNum(), group);
    }

    @Override
    public Field<?> get(int tagNum) {
        Field<?> field = fields.get(tagNum);
        if (field != null) {
            return field;
        }
        Group group = groups.get(tagNum);
        if (group != null) {
            return group.getNumInGroup();
        }
        ComponentMember containingComponentMember = metadata.getComponentMemberContaining(tagNum);
        if (containingComponentMember != null) {
            Component component = components.get(containingComponentMember.getMetadata().getName());
            return component.get(tagNum);
        }
        return null;
    }

    @Override
    public Collection<Field<?>> getFields() {
        return fields.valueCollection();
    }

    @Override
    public void set(Field<?> field) {
        fields.put(field.getMetadata().getTagNum(), field);
    }

    @Override
    public void addUdf(Field<?> tagValue) {
        udfs.add(tagValue);
    }

    @Override
    public List<Field<?>> getUdfs() {
        return udfs;
    }

    @Override
    public boolean isEmpty() {
        return components.isEmpty() && groups.isEmpty() && fields.isEmpty() && udfs.isEmpty();
    }
}
