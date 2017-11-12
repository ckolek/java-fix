package me.kolek.fix.serialization.metadata;

import me.kolek.fix.serialization.Message;
import me.kolek.util.exception.CannotHappenException;
import me.kolek.util.io.WriterHelper;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

/**
 * @author ckolek
 */
public class MessageMetadata extends StructureMetadata<Message> {
    private final String msgType;

    public MessageMetadata(long id, String name, String msgType, List<StructureMember> members) {
        super(id, name, members);
        this.msgType = msgType;
    }

    public String getMsgType() {
        return msgType;
    }

    @Override
    public Message newStructure() {
        return new Message(this);
    }

    @Override
    String toString(Message message) {
        WriterHelper writer = new WriterHelper(new StringWriter());
        try {
            return toString(message,
                    writer.write(getName()).write('(').write(msgType).write("):").newLine().addIndent()).toString();
        } catch (IOException e) {
            throw new CannotHappenException(e);
        }
    }
}
