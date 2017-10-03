package me.kolek.fix.serialization;

import me.kolek.fix.TagValue;
import me.kolek.fix.serialization.field.*;
import me.kolek.fix.serialization.metadata.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ckolek
 */
public class FixSerialization {
    public static void main(String[] args) {
        MessageMetadata messageMetadata =
                message("TestMessage", "XXX").component("TestComponent").field(10001, "TestField1", new StringSerDes())
                        .field(10002, "TestField2", new IntSerDes(null)).field(10003, "TestField3", new BooleanSerDes())
                        .end().group("TestGroup", 10101).field(10004, "TestField4", new CharSerDes())
                        .field(10005, "TestField5", new MultipleCharValueSerDes())
                        .field(10006, "TestField6", new MultipleStringValueSerDes()).end()
                        .field(10007, "TestField7", new DataSerDes())
                        .field(10008, "TestField8", new LocalMktDateSerDes("yyyyMMdd"))
                        .field(10009, "TestField9", new FloatSerDes(10.0, "0.####")).build();

        List<TagValue> tagValues =
                tagValues().add(10101, 3).add(10004, 'X').add(10005, 'a', 'b', 'c').add(10006, "A1", "B2", "C3")
                        .add(10004, 'Y').add(10005, 'd', 'e', 'f').add(10006, "D4", "E5", "F6").add(10004, 'Z')
                        .add(10005, 'g', 'h', 'i').add(10006, "G7", "H8", "I9").add(10007, "some text")
                        .add(10001, "test value").add(10008, LocalDate.of(1992, 1, 7)).add(10002, 100).add(10009, 3.14)
                        .add(10003, true).build();

        long time1 = System.nanoTime();

        Message message = messageMetadata.deserialize(tagValues);

        System.out.println(message);

        long time2 = System.nanoTime();

        List<TagValue> tagValues2 = message.toTagValues();

        long time3 = System.nanoTime();

        System.out.println(tagValues2);
        System.out.printf("deserialize time (s): %10.6f\n", (time2 - time1) / 1_000_000_000.0);
        System.out.printf("  serialize time (s): %10.6f\n", (time3 - time2) / 1_000_000_000.0);
    }

    public static MessageMetadataBuilder message(String name, String msgType) {
        return new MessageMetadataBuilder(name, msgType);
    }

    public static TagValuesBuilder tagValues() {
        return new TagValuesBuilder();
    }

    public static class StructureMetadataBuilder<Parent extends StructureMetadataBuilder<?, ?>, Self extends
            StructureMetadataBuilder<?, ?>> {
        protected final Parent parent;
        protected final List<StructureMember> members;

        public StructureMetadataBuilder(Parent parent) {
            this.parent = parent;
            this.members = new ArrayList<>();
        }

        public ComponentMetadataBuilder<Self> component(String name) {
            return component(name, false, false);
        }

        public ComponentMetadataBuilder<Self> component(String name, boolean trailing, boolean required) {
            return new ComponentMetadataBuilder<>((Self) this, name, trailing, required);
        }

        public GroupMetadataBuilder<Self> group(String name, int numInGroupTagNum) {
            return group(name, numInGroupTagNum, false, false);
        }

        public GroupMetadataBuilder<Self> group(String name, int numInGroupTagNum, boolean trailing, boolean required) {
            return new GroupMetadataBuilder<>((Self) this, name, numInGroupTagNum, trailing, required);
        }

        public Self field(int tagNum, String name, FieldSerDes<?> serDes) {
            return field(tagNum, name, serDes, false, false);
        }

        public Self field(int tagNum, String name, FieldSerDes<?> serDes, boolean trailing, boolean required) {
            members.add(
                    new FieldMember(new FieldMetadata<>(0, tagNum, name, serDes), members.size(), trailing, required));
            return (Self) this;
        }

        public Parent end() {
            return parent;
        }
    }

    public static class MessageMetadataBuilder
            extends StructureMetadataBuilder<MessageMetadataBuilder, MessageMetadataBuilder> {
        private final String name;
        private final String msgType;

        public MessageMetadataBuilder(String name, String msgType) {
            super(null);
            this.name = name;
            this.msgType = msgType;
        }

        @Override
        public MessageMetadataBuilder end() {
            return this;
        }

        public MessageMetadata build() {
            return new MessageMetadata(0, name, msgType, members);
        }
    }

    public static class ComponentMetadataBuilder<S extends StructureMetadataBuilder<?, ?>>
            extends StructureMetadataBuilder<S, ComponentMetadataBuilder<S>> {
        private final String name;
        private final boolean trailing;
        private final boolean required;

        public ComponentMetadataBuilder(S parent, String name, boolean trailing, boolean required) {
            super(parent);
            this.name = name;
            this.trailing = trailing;
            this.required = required;
        }

        @Override
        public S end() {
            parent.members
                    .add(new ComponentMember(new ComponentMetadata(0, name, members), parent.members.size(), trailing,
                            required));
            return super.end();
        }
    }

    public static class GroupMetadataBuilder<S extends StructureMetadataBuilder<?, ?>>
            extends StructureMetadataBuilder<S, GroupMetadataBuilder<S>> {
        private final String name;
        private final int numInGroupTagNum;
        private final boolean trailing;
        private final boolean required;

        public GroupMetadataBuilder(S parent, String name, int numInGroupTagNum, boolean trailing, boolean required) {
            super(parent);
            this.name = name;
            this.numInGroupTagNum = numInGroupTagNum;
            this.trailing = trailing;
            this.required = required;
        }

        @Override
        public S end() {
            parent.members.add(new GroupMember(new GroupMetadata(0, name,
                    new FieldMetadata<>(0, numInGroupTagNum, "No" + name + "s", new IntSerDes(null)), members),
                    parent.members.size(), trailing, required));
            return super.end();
        }
    }

    public static class TagValuesBuilder {
        private final List<TagValue> tagValues;

        public TagValuesBuilder() {
            this.tagValues = new ArrayList<>();
        }

        public TagValuesBuilder add(int tagNum, boolean value) {
            return add(tagNum, value ? "Y" : "N");
        }

        public TagValuesBuilder add(int tagNum, char value) {
            return add(tagNum, Character.toString(value));
        }

        public TagValuesBuilder add(int tagNum, char... values) {
            StringBuilder string = new StringBuilder();
            for (int i = 0; i < values.length; i++) {
                string.append(values[i]);
                if (i < values.length - 1) {
                    string.append(' ');
                }
            }
            return add(tagNum, string.toString());
        }

        public TagValuesBuilder add(int tagNum, double value) {
            return add(tagNum, Double.toString(value));
        }

        public TagValuesBuilder add(int tagNum, float value) {
            return add(tagNum, Float.toString(value));
        }

        public TagValuesBuilder add(int tagNum, int value) {
            return add(tagNum, Integer.toString(value));
        }

        public TagValuesBuilder add(int tagNum, long value) {
            return add(tagNum, Long.toString(value));
        }

        public TagValuesBuilder add(int tagNum, short value) {
            return add(tagNum, Short.toString(value));
        }

        public TagValuesBuilder add(int tagNum, String value) {
            tagValues.add(new TagValue(tagNum, value));
            return this;
        }

        public TagValuesBuilder add(int tagNum, String... values) {
            return add(tagNum, String.join(" ", values));
        }

        public TagValuesBuilder add(int tagNum, LocalDate value) {
            return add(tagNum, value, "yyyyMMdd");
        }

        public TagValuesBuilder add(int tagNum, LocalTime value) {
            return add(tagNum, value, "HH:mm:ss.nnnnnn");
        }

        public TagValuesBuilder add(int tagNum, LocalDateTime value) {
            return add(tagNum, value, "yyyyMMdd-HH:mm:ss.nnnnnn");
        }

        public TagValuesBuilder add(int tagNum, Temporal value, String format) {
            return add(tagNum, value, DateTimeFormatter.ofPattern(format));
        }

        public TagValuesBuilder add(int tagNum, Temporal value, DateTimeFormatter formatter) {
            return add(tagNum, formatter.format(value));
        }

        public List<TagValue> build() {
            return tagValues;
        }
    }
}
