package me.kolek.fix.repository;

public class Field {
    private final int tagNum;
    private final String name;
    private final String type;
    private final String description;

    public Field(int tagNum, String name, String type, String description) {
        this.tagNum = tagNum;
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public int getTagNum() {
        return tagNum;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
