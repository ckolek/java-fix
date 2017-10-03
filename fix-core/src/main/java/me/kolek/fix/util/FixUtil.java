package me.kolek.fix.util;

import me.kolek.fix.constants.ApplVerId;
import me.kolek.fix.constants.BeginString;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FixUtil {
    private static final Map<String, String> applVerIdToBeginString;
    private static final Map<String, String> beginStringToApplVerId;

    static {
        Map<String, String> _applVerIdToBeginString = new HashMap<>();
        Map<String, String> _beginStringToApplVerId = new HashMap<>();
        Arrays.stream(new String[][]{
                {ApplVerId.FIX27, BeginString.FIX27},
                {ApplVerId.FIX30, BeginString.FIX30},
                {ApplVerId.FIX40, BeginString.FIX40},
                {ApplVerId.FIX41, BeginString.FIX41},
                {ApplVerId.FIX42, BeginString.FIX42},
                {ApplVerId.FIX43, BeginString.FIX43},
                {ApplVerId.FIX44, BeginString.FIX44},
                {ApplVerId.FIX50, BeginString.FIX50},
                {ApplVerId.FIX50SP1, BeginString.FIX50SP1},
                {ApplVerId.FIX50SP2, BeginString.FIX50SP2},
        }).forEach(applVerIdBeginString -> {
            _applVerIdToBeginString.put(applVerIdBeginString[0], applVerIdBeginString[1]);
            _beginStringToApplVerId.put(applVerIdBeginString[1], applVerIdBeginString[0]);
        });
        applVerIdToBeginString = Collections.unmodifiableMap(_applVerIdToBeginString);
        beginStringToApplVerId = Collections.unmodifiableMap(_beginStringToApplVerId);
    }

    public static String toBeginString(String applVerId) {
        return applVerIdToBeginString.get(applVerId);
    }

    public static String toApplVerId(String beginString) {
        return beginStringToApplVerId.get(beginString);
    }
}
