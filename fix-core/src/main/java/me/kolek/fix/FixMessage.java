package me.kolek.fix;

import me.kolek.fix.constants.TagNum;
import me.kolek.fix.util.FixUtil;

import java.io.Serializable;
import java.util.*;

public class FixMessage implements Iterable<TagValue>, Serializable {
    public static final char FIELD_DELIMITER = 0x01;

    private final List<TagValue> tagValues;

    public FixMessage() {
        this.tagValues = new LinkedList<>();
    }

    public FixMessage(List<TagValue> tagValues) {
        this.tagValues = new LinkedList<>(tagValues);
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

    public void addAll(Collection<TagValue> tagValues) {
        this.tagValues.addAll(tagValues);
    }

    public void addAll(int index, Collection<TagValue> tagValues) {
        this.tagValues.addAll(index, tagValues);
    }

    public TagValue get(int index) {
        return tagValues.get(index);
    }

    public OptionalInt indexOf(int tagNum) {
        return indexOf(tagNum, 0);
    }

    public OptionalInt indexOf(int tagNum, int index) {
        int current = 0;
        for (ListIterator<TagValue> iter = tagValues.listIterator(); iter.hasNext() && current <= index;) {
            TagValue tagValue = iter.next();
            if (tagValue.getTagNum() == tagNum && current++ == index) {
                return OptionalInt.of(iter.previousIndex());
            }
        }
        return OptionalInt.empty();
    }

    public Optional<String> getValue(int tagNum) {
        return getValue(tagNum, 0);
    }

    public Optional<String> getValue(int tagNum, int index) {
        OptionalInt fieldIndex = indexOf(tagNum, index);
        if (fieldIndex.isPresent()) {
            return Optional.of(tagValues.get(fieldIndex.getAsInt()).getValue());
        } else {
            return Optional.empty();
        }
    }

    public Optional<String> getBeginString() {
        return getValue(TagNum.BeginString);
    }

    public Optional<String> getBodyLength() {
        return getValue(TagNum.BodyLength);
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

    public Optional<String> getMsgSeqNum() {
        return getValue(TagNum.MsgSeqNum);
    }

    public Optional<String> getSendingTime() {
        return getValue(TagNum.SendingTime);
    }

    public Optional<String> getCheckSum() {
        return getValue(TagNum.CheckSum);
    }

    public TagValue remove(int index) {
        return tagValues.remove(index);
    }

    public boolean isEmpty() {
        return tagValues.isEmpty();
    }

    public int size() {
        return tagValues.size();
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
        return FixUtil.toString(tagValues);
    }
}
