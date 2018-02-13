package me.kolek.fix.serialization.metadata;

import me.kolek.fix.TagValue;
import me.kolek.fix.serialization.Component;
import me.kolek.fix.serialization.Field;
import me.kolek.fix.serialization.Group;

import java.util.Collection;
import java.util.List;

public abstract class AbstractStructure<MD extends StructureMetadata> {
    protected final MD metadata;

    public AbstractStructure(MD metadata) {
        this.metadata = metadata;
    }

    public MD getMetadata() {
        return metadata;
    }

    public abstract Component getComponent(String name);

    public abstract Component getOrCreateComponent(String name);

    public abstract Collection<Component> getComponents();

    public abstract void set(Component component);

    public abstract Group getGroup(String name);

    public abstract Group getOrCreateGroup(String name);

    public abstract Group getGroup(int numInGroupTagNum);

    public abstract Group getOrCreateGroup(int numInGroupTagNum);

    public abstract Collection<Group> getGroups();

    public abstract void set(Group group);

    public abstract Field<?> get(int tagNum);

    public abstract Collection<Field<?>> getFields();

    public abstract void set(Field<?> field);

    public abstract Collection<Field<?>> getUdfs();

    public abstract void addUdf(Field<?> udf);

    public abstract boolean isEmpty();

    public List<TagValue> toTagValues() {
        return metadata.serialize(this);
    }

    @Override
    public String toString() {
        return metadata.toString(this);
    }
}
