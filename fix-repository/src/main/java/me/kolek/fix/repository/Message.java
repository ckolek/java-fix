package me.kolek.fix.repository;

public class Message {
    private final int id;
    private final String msgType;
    private final String name;
    private final String description;

    public Message(int id, String msgType, String name, String description) {
        this.id = id;
        this.msgType = msgType;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getMsgType() {
        return msgType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
