package me.kolek.fix.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Constants for the values of the BeginString(8) field.
 */
public enum BeginString {
    FIX27(BeginString._FIX27, ApplVerId.FIX27),
    FIX30(BeginString._FIX30, ApplVerId.FIX30),
    FIX40(BeginString._FIX40, ApplVerId.FIX40),
    FIX41(BeginString._FIX41, ApplVerId.FIX41),
    FIX42(BeginString._FIX42, ApplVerId.FIX42),
    FIX43(BeginString._FIX43, ApplVerId.FIX43),
    FIX44(BeginString._FIX44, ApplVerId.FIX44),
    FIX50(BeginString._FIX50, ApplVerId.FIX50),
    FIX50SP1(BeginString._FIX50SP1, ApplVerId.FIX50SP1),
    FIX50SP2(BeginString._FIX50SP2, ApplVerId.FIX50SP2),
    FIXT11(BeginString._FIXT11, null);

    public static final String _FIX27 = "FIX.2.7";
    public static final String _FIX30 = "FIX.3.0";
    public static final String _FIX40 = "FIX.4.0";
    public static final String _FIX41 = "FIX.4.1";
    public static final String _FIX42 = "FIX.4.2";
    public static final String _FIX43 = "FIX.4.3";
    public static final String _FIX44 = "FIX.4.4";
    public static final String _FIX50 = "FIX.5.0";
    public static final String _FIX50SP1 = "FIX.5.0SP1";
    public static final String _FIX50SP2 = "FIX.5.0SP2";
    public static final String _FIXT11 = "FIXT.1.1";

    private final String value;
    private final ApplVerId applVerId;
    private final boolean isTransport;
    private final int major;
    private final int minor;
    private final int servicePack;

    BeginString(String value, ApplVerId applVerId) {
        this.value = value;
        this.applVerId = applVerId;

        Matcher matcher = Pattern.compile("FIX(T)?\\.(\\d+)\\.(\\d+)(SP(\\d+))").matcher(value);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("value does not match accepted pattern");
        }

        this.isTransport = matcher.group(1) != null;
        this.major = Integer.parseInt(matcher.group(2));
        this.minor = Integer.parseInt(matcher.group(3));
        this.servicePack = matcher.group(4) != null ? Integer.parseInt(matcher.group(5)) : -1;
    }

    public String value() {
        return value;
    }

    public ApplVerId getApplVerId() {
        return applVerId;
    }

    public boolean isTransport() {
        return isTransport;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getServicePack() {
        return servicePack;
    }

    private static final Map<String, BeginString> byValue = Collections.unmodifiableMap(
            Arrays.stream(values()).collect(Collectors.toMap(BeginString::value, Function.identity())));

    public static Optional<BeginString> fromValue(String value) {
        return Optional.ofNullable(byValue.get(value));
    }
}
