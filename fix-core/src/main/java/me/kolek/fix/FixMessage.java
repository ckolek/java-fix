package me.kolek.fix;

import me.kolek.fix.constants.TagNum;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class FixMessage implements Iterable<TagValue>, Serializable {
    public static final char FIELD_DELIMITER = 0x01;

    private final List<TagValue> tagValues;

    public FixMessage() {
        this.tagValues = new ArrayList<>();
    }

    public FixMessage(List<TagValue> tagValues) {
        this.tagValues = new ArrayList<>(tagValues);
    }

    public void add(int tag, String value) {
        add(new TagValue(tag, value));
    }

    public void add(TagValue tagValue) {
        tagValues.add(tagValue);
    }

    public void add(int index, int tag, String value) {
        add(index, new TagValue(tag, value));
    }

    public void add(int index, TagValue tagValue) {
        tagValues.add(index, tagValue);
    }

    public TagValue get(int index) {
        return tagValues.get(index);
    }

    public Optional<String> getValue(int tagNum) {
        return getValue(tagNum, 0);
    }

    public Optional<String> getValue(int tagNum, int index) {
        int current = 0;
        for (Iterator<TagValue> iter = tagValues.iterator(); iter.hasNext() && current <= index;) {
            TagValue tagValue = iter.next();
            if (tagValue.getTagNum() == tagNum && current++ == index) {
                return Optional.of(tagValue.getValue());
            }
        }
        return Optional.empty();
    }

    public Optional<String> getBeginString() {
        return getValue(TagNum.BeginString);
    }

    public Optional<String> getMsgType() {
        return getValue(TagNum.MsgType);
    }

    public Optional<String> getSenderCompID() {
        return getValue(TagNum.SenderCompID);
    }

    public Optional<String> getSenderSubID() {
        return getValue(TagNum.SenderSubID);
    }

    public Optional<String> getSenderLocationID() {
        return getValue(TagNum.SenderLocationID);
    }

    public Optional<String> getTargetCompID() {
        return getValue(TagNum.TargetCompID);
    }

    public Optional<String> getTargetSubID() {
        return getValue(TagNum.TargetSubID);
    }

    public Optional<String> getTargetLocationID() {
        return getValue(TagNum.TargetLocationID);
    }

    public Optional<String> getCheckSum() {
        return getValue(TagNum.CheckSum);
    }

    public TagValue remove(int index) {
        return tagValues.remove(index);
    }

    public void clear() {
        tagValues.clear();
    }

    public List<TagValue> getTagValues() {
        return tagValues;
    }

    @Override
    public Iterator<TagValue> iterator() {
        return tagValues.iterator();
    }

    public ListIterator<TagValue> listIterator() {
        return tagValues.listIterator();
    }

    @Override
    public String toString() {
        return tagValues.stream().map(Object::toString).map(field -> field + FIELD_DELIMITER)
                .collect(Collectors.joining());
    }
}
