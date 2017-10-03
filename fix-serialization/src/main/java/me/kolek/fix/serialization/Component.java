package me.kolek.fix.serialization;

import me.kolek.fix.serialization.metadata.ComponentMetadata;

/**
 * @author ckolek
 */
public class Component extends Structure<ComponentMetadata> {
    public Component(ComponentMetadata metadata, Structure<?> parent) {
        super(metadata, parent);
    }
}
