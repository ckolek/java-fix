package me.kolek.fix;

import com.google.common.base.Strings;
import me.kolek.fix.util.FixUtil;

import java.io.Serializable;

public class TagValue implements Serializable {
    public static final char SEPARATOR = '=';

    private final int tagNum;
    private final String value;

    public TagValue(int tagNum, String value) {
        if (Strings.isNullOrEmpty(value)) {
            throw new IllegalArgumentException("value cannot be null or empty");
        }
        this.tagNum = tagNum;
        this.value = value;
    }

    public int getTagNum() {
        return tagNum;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return FixUtil.toString(this);
    }
}
