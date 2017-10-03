package me.kolek.fix.repository;

import java.util.function.Consumer;

public abstract class Content {
    private final int ownerId;
    private final int position;
    private final boolean required;

    protected Content(int ownerId, int position, boolean required) {
        this.ownerId = ownerId;
        this.position = position;
        this.required = required;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public int getPosition() {
        return position;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean asComponent(Consumer<ComponentContent> action) {
        if (this instanceof ComponentContent) {
            action.accept((ComponentContent) this);
            return true;
        } else {
            return false;
        }
    }

    public boolean asField(Consumer<FieldContent> action) {
        if (this instanceof FieldContent) {
            action.accept((FieldContent) this);
            return true;
        } else {
            return false;
        }
    }

    public static class ComponentContent extends Content {
        private final String name;

        public ComponentContent(int ownerId, int position, boolean required, String name) {
            super(ownerId, position, required);
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class FieldContent extends Content {
        private final int tagNum;

        public FieldContent(int ownerId, int position, boolean required, int tagNum) {
            super(ownerId, position, required);
            this.tagNum = tagNum;
        }

        public int getTagNum() {
            return tagNum;
        }
    }
}
