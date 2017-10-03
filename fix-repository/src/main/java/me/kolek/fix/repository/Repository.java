package me.kolek.fix.repository;

import me.kolek.fix.repository.xml.RepositoryParser;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Repository {
    private final String version;
    private final Map<String, List<DataType>> dataTypesByBaseType;
    private final Map<Integer, Message> messagesById;
    private final Map<String, Component> componentsByName;
    private final Map<Integer, Field> fieldsByTagNum;
    private final Map<Integer, List<Content>> contentsByOwnerId;

    public Repository(String version, Stream<DataType> dataTypes, Stream<Message> messages,
            Stream<Component> components, Stream<Field> fields, Stream<Content> contents) {
        this.version = version;
        this.dataTypesByBaseType =
                dataTypes.collect(Collectors.groupingBy(dt -> Objects.toString(dt.getBaseType(), "")));
        this.messagesById = messages.collect(Collectors.toMap(Message::getId, Function.identity()));
        this.componentsByName = components.collect(Collectors.toMap(Component::getName, Function.identity()));
        this.fieldsByTagNum = fields.collect(Collectors.toMap(Field::getTagNum, Function.identity()));
        this.contentsByOwnerId = contents.sorted(Comparator.comparingInt(Content::getPosition))
                .collect(Collectors.groupingBy(Content::getOwnerId));
    }

    public String getVersion() {
        return version;
    }

    public Collection<DataType> getRootDataTypes() {
        return dataTypesByBaseType.get("");
    }

    public Collection<DataType> getDataTypes(DataType baseType) {
        return dataTypesByBaseType.getOrDefault(baseType.getName(), Collections.emptyList());
    }

    public Collection<Message> getMessages() {
        return messagesById.values();
    }

    public Message getMessage(int id) {
        return messagesById.get(id);
    }

    public Collection<Component> getComponents() {
        return componentsByName.values();
    }

    public Component getComponent(String name) {
        return componentsByName.get(name);
    }

    public Collection<Field> getFields() {
        return fieldsByTagNum.values();
    }

    public Field getField(int tagNum) {
        return fieldsByTagNum.get(tagNum);
    }

    public List<Content> getContents(Message message) {
        return contentsByOwnerId.get(message.getId());
    }

    public List<Content> getContents(Component component) {
        return contentsByOwnerId.get(component.getId());
    }

    public static Repository parse(File repositoryDirectory) throws Exception {
        return new RepositoryParser().parse(repositoryDirectory);
    }
}
