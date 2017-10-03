package me.kolek.fix.serialization.metadata;

import me.kolek.fix.serialization.Component;

import java.util.List;

/**
 * @author ckolek
 */
public class ComponentMetadata extends StructureMetadata<Component> {
    public ComponentMetadata(long id, String name, List<StructureMember> members) {
        super(id, name, members);
    }

    @Override
    public Component newStructure() {
        return new Component(this, null);
    }
}
