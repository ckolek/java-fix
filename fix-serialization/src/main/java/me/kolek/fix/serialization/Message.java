package me.kolek.fix.serialization;

import me.kolek.fix.serialization.metadata.MessageMetadata;

/**
 * @author ckolek
 */
public class Message extends Structure<MessageMetadata> {
    public Message(MessageMetadata metadata) {
        super(metadata, null);
    }
}
