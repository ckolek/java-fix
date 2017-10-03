package me.kolek.fix.repository;

public class DataType {
    private final String name;
    private final String baseType;
    private final String description;

    public DataType(String name, String baseType, String description) {
        this.name = name;
        this.baseType = baseType;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getBaseType() {
        return baseType;
    }

    public String getDescription() {
        return description;
    }
}
