package me.kolek.fix.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Constants for the values of the ApplVerId(1128) field.
 */
public enum ApplVerId {
    FIX27(ApplVerId._FIX27, BeginString.FIX27),
    FIX30(ApplVerId._FIX30, BeginString.FIX30),
    FIX40(ApplVerId._FIX40, BeginString.FIX40),
    FIX41(ApplVerId._FIX41, BeginString.FIX41),
    FIX42(ApplVerId._FIX42, BeginString.FIX42),
    FIX43(ApplVerId._FIX43, BeginString.FIX43),
    FIX44(ApplVerId._FIX44, BeginString.FIX44),
    FIX50(ApplVerId._FIX50, BeginString.FIX50),
    FIX50SP1(ApplVerId._FIX50SP1, BeginString.FIX50SP1),
    FIX50SP2(ApplVerId._FIX50SP2, BeginString.FIX50SP2);

    public static final String _FIX27 = "0";
    public static final String _FIX30 = "1";
    public static final String _FIX40 = "2";
    public static final String _FIX41 = "3";
    public static final String _FIX42 = "4";
    public static final String _FIX43 = "5";
    public static final String _FIX44 = "6";
    public static final String _FIX50 = "7";
    public static final String _FIX50SP1 = "8";
    public static final String _FIX50SP2 = "9";

    private final String value;
    private final BeginString beginString;

    ApplVerId(String value, BeginString beginString) {
        this.value = value;
        this.beginString = beginString;
    }

    public String value() {
        return value;
    }

    public BeginString getBeginString() {
        return beginString;
    }

    private static final Map<String, ApplVerId> byValue = Collections.unmodifiableMap(
            Arrays.stream(values()).collect(Collectors.toMap(ApplVerId::value, Function.identity())));

    public static Optional<ApplVerId> fromValue(String value) {
        return Optional.ofNullable(byValue.get(value));
    }
}
