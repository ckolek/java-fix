package me.kolek.fix.repository;

public class Component {
    private final int id;
    private final String type;
    private final String name;
    private final String description;

    public Component(int id, String type, String name, String description) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
