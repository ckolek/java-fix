package me.kolek.fix.serialization;

import me.kolek.fix.serialization.metadata.FieldValue;
import me.kolek.fix.serialization.metadata.GroupMetadata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author ckolek
 */
public class Group implements Iterable<Group.Element> {
    private final GroupMetadata metadata;
    private final Structure<?> parent;

    private final List<Element> elements;

    private FieldValue<Integer> numInGroup;

    public Group(GroupMetadata metadata, Structure<?> parent) {
        this.metadata = metadata;
        this.parent = parent;
        this.elements = new ArrayList<>();
        this.numInGroup = metadata.getNumInGroupField().toFieldValue(0);
    }

    public GroupMetadata getMetadata() {
        return metadata;
    }

    public Structure<?> getParent() {
        return parent;
    }

    public int size() {
        return elements.size();
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public Element get(int index) {
        return elements.get(index);
    }

    public List<Element> elements() {
        return elements;
    }

    public Element newElement() {
        Element element = new Element(metadata);
        elements.add(element);
        numInGroup = metadata.getNumInGroupField().toFieldValue(elements.size());
        return element;
    }

    public FieldValue<Integer> getNumInGroup() {
        return numInGroup;
    }

    @Override
    public Iterator<Element> iterator() {
        return elements.iterator();
    }

    public class Element extends Structure<GroupMetadata> {
        private Element(GroupMetadata metadata) {
            super(metadata, parent);
        }

        public Group getGroup() {
            return Group.this;
        }
    }
}
