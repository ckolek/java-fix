package me.kolek.fix;

import me.kolek.fix.constants.Field;

import java.io.Serializable;

public interface FixDictionary extends Serializable {
    Field.FieldType getFieldType(int tagNum);

    boolean isGroup(int tagNum);

    int[] getTagNums(String msgType);

    int[] getTagNums(int numInGroupTagNum);
}
