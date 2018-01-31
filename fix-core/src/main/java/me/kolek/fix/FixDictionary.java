package me.kolek.fix;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import me.kolek.fix.constants.BeginString;
import me.kolek.fix.constants.FieldType;

import java.io.Serializable;
import java.util.*;

public class FixDictionary implements Serializable {
    private final Map<BeginString, Version> versions;

    private FixDictionary(Map<BeginString, Version> versions) {
        this.versions = Collections.unmodifiableMap(versions);
    }

    public Collection<Version> getVersions() {
        return versions.values();
    }

    public Version getVersion(BeginString beginString) {
        return versions.get(beginString);
    }

    public static class Version {
        private final BeginString beginString;
        private final Map<String, Message> messagesByName;
        private final Map<String, Message> messagesByMsgType;
        private final Map<String, Component> componentsByName;
        private final Map<String, Group> groupsByName;
        private final TIntObjectMap<Group> groupsByTagNum;
        private final Map<String, Field> fieldsByName;
        private final TIntObjectMap<Field> fieldsByTagNum;

        private Version(BeginString beginString) {
            this.beginString = beginString;
            this.messagesByName = new HashMap<>();
            this.messagesByMsgType = new HashMap<>();
            this.componentsByName = new HashMap<>();
            this.groupsByName = new HashMap<>();
            this.groupsByTagNum = new TIntObjectHashMap<>();
            this.fieldsByName = new HashMap<>();
            this.fieldsByTagNum = new TIntObjectHashMap<>();
        }

        public BeginString getBeginString() {
            return beginString;
        }

        public Collection<Message> getMessages() {
            return messagesByName.values();
        }

        public Message getMessage(String name) {
            return messagesByName.get(name);
        }

        public Message getMessageByMsgType(String msgType) {
            return messagesByMsgType.get(msgType);
        }

        public boolean isAdminMessage(String msgType) {
            Message message = getMessage(msgType);
            return message != null && Message.MSG_CAT__ADMIN.equals(message.getMsgCategory());
        }

        public boolean isApplicationMessage(String msgType) {
            Message message = getMessage(msgType);
            return message != null && Message.MSG_CAT__APP.equals(message.getMsgCategory());
        }

        public Collection<Component> getComponents() {
            return componentsByName.values();
        }

        public Component getComponent(String name) {
            return componentsByName.get(name);
        }

        public Collection<Group> getGroups() {
            return groupsByName.values();
        }

        public Group getGroup(String name) {
            return groupsByName.get(name);
        }

        public Group getGroup(int numInGroupTagNum) {
            return groupsByTagNum.get(numInGroupTagNum);
        }

        public Collection<Field> getFields() {
            return fieldsByName.values();
        }

        public Field getField(String name) {
            return fieldsByName.get(name);
        }

        public Field getField(int tagNum) {
            return fieldsByTagNum.get(tagNum);
        }

        public enum ElementType {
            COMPONENT, GROUP, FIELD;
        }

        public static class Element {
            private final String name;
            private final ElementType type;
            private final boolean required;

            private Element(String name, ElementType type, boolean required) {
                this.name = name;
                this.type = type;
                this.required = required;
            }

            public String getName() {
                return name;
            }

            public ElementType getType() {
                return type;
            }

            public boolean isRequired() {
                return required;
            }
        }

        public class ElementContainer {
            private final List<Element> elements;
            private int[] tagNums;

            private ElementContainer() {
                this.elements = new ArrayList<>();
            }

            public List<Element> getElements() {
                return elements;
            }

            public int[] getTagNums() {
                if (tagNums == null) {
                    TIntList tagNums = new TIntArrayList();
                    collectTagNums(tagNums);
                    this.tagNums = tagNums.toArray();
                }
                return tagNums;
            }

            private void collectTagNums(TIntList tagNums) {
                for (Element element : elements) {
                    switch (element.type) {
                        case COMPONENT:
                            Component component = componentsByName.get(element.name);
                            ((ElementContainer) component).collectTagNums(tagNums);
                            break;
                        case GROUP:
                            Group group = groupsByName.get(element.name);
                            tagNums.add(group.tagNum);
                            ((ElementContainer) group).collectTagNums(tagNums);
                            break;
                        case FIELD:
                            Field field = fieldsByName.get(element.name);
                            tagNums.add(field.tagNum);
                            break;
                    }
                }
            }
        }

        public class Message extends ElementContainer {
            public static final String MSG_CAT__ADMIN = "admin";
            public static final String MSG_CAT__APP = "app";

            private final String name;
            private final String msgType;
            private final String msgCategory;

            private Message(String name, String msgType, String msgCategory) {
                this.name = name;
                this.msgType = msgType;
                this.msgCategory = msgCategory;
            }

            public String getName() {
                return name;
            }

            public String getMsgType() {
                return msgType;
            }

            public String getMsgCategory() {
                return msgCategory;
            }
        }

        public class Component extends ElementContainer {
            private final String name;

            private Component(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

        public class Group extends ElementContainer {
            private final String name;
            private final int tagNum;

            private Group(String name, int tagNum) {
                this.name = name;
                this.tagNum = tagNum;
            }

            public String getName() {
                return name;
            }

            public int getNumInGroupTagNum() {
                return tagNum;
            }
        }

        public class Field {
            private final String name;
            private final int tagNum;
            private final FieldType type;

            private Field(String name, int tagNum, FieldType type) {
                this.name = name;
                this.tagNum = tagNum;
                this.type = type;
            }

            public String getName() {
                return name;
            }

            public FieldType getType() {
                return type;
            }

            public int getTagNum() {
                return tagNum;
            }
        }
    }

    public static final class Builder {
        private final Map<BeginString, Version> versions = new HashMap<>();

        public VersionBuilder addVersion(BeginString beginString) {
            Version version = new Version(beginString);
            versions.put(beginString, version);
            return new VersionBuilder(version);
        }

        public FixDictionary build() {
            return new FixDictionary(versions);
        }
    }

    public static final class VersionBuilder {
        private final Version version;

        private VersionBuilder(Version version) {
            this.version = version;
        }

        public ElementContainerBuilder addMessage(String name, String msgType, String msgCategory) {
            Version.Message message = version.new Message(name, msgType, msgCategory);
            version.messagesByName.put(name, message);
            version.messagesByMsgType.put(msgType, message);
            return new ElementContainerBuilder(message);
        }

        public ElementContainerBuilder addComponent(String name) {
            Version.Component component = version.new Component(name);
            version.componentsByName.put(name, component);
            return new ElementContainerBuilder(component);
        }

        public ElementContainerBuilder addGroup(String name, int numInGroupTagNum) {
            Version.Group group = version.new Group(name, numInGroupTagNum);
            version.groupsByName.put(name, group);
            version.groupsByTagNum.put(numInGroupTagNum, group);
            return new ElementContainerBuilder(group);
        }

        public VersionBuilder addField(String name, int tagNum, FieldType type) {
            Version.Field field = version.new Field(name, tagNum, type);
            version.fieldsByName.put(name, field);
            version.fieldsByTagNum.put(tagNum, field);
            return this;
        }
    }

    public static final class ElementContainerBuilder {
        private final Version.ElementContainer container;

        private ElementContainerBuilder(Version.ElementContainer container) {
            this.container = container;
        }

        public ElementContainerBuilder addElement(String name, Version.ElementType type, boolean required) {
            container.elements.add(new Version.Element(name, type, required));
            return this;
        }

        public ElementContainerBuilder addComponentElement(String name, boolean required) {
            return addElement(name, Version.ElementType.COMPONENT, required);
        }

        public ElementContainerBuilder addGroupElement(String name, boolean required) {
            return addElement(name, Version.ElementType.GROUP, required);
        }

        public ElementContainerBuilder addFieldElement(String name, boolean required) {
            return addElement(name, Version.ElementType.FIELD, required);
        }
    }
}
