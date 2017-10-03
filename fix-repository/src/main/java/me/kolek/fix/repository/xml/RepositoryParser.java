package me.kolek.fix.repository.xml;

import me.kolek.fix.repository.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Optional;

public class RepositoryParser {
    private final JAXBContext context;
    private final Unmarshaller unmarshaller;

    public RepositoryParser() throws JAXBException {
        this.context = JAXBContext.newInstance("me.kolek.fix.repository.xml");
        this.unmarshaller = context.createUnmarshaller();
    }

    public Repository parse(File repoDir) throws Exception {
        Datatypes datatypes = parseDatatypes(repoDir).orElseThrow(() -> new Exception("Datatypes file not found"));
        Messages messages = parseMessages(repoDir).orElseThrow(() -> new Exception("Messages file not found"));
        Components components = parseComponents(repoDir).orElseThrow(() -> new Exception("Components file not found"));
        Fields fields = parseFields(repoDir).orElseThrow(() -> new Exception("Fields file not found"));
        MsgContents msgContents =
                parseMsgContents(repoDir).orElseThrow(() -> new Exception("MsgContents file not found"));

        return new Repository(datatypes.version, datatypes.datatype.stream().map(RepositoryParser::toDataType),
                messages.message.stream().map(RepositoryParser::toMessage),
                components.component.stream().map(RepositoryParser::toComponent),
                fields.field.stream().map(RepositoryParser::toField),
                msgContents.msgContent.stream().map(RepositoryParser::toContent));
    }

    private Optional<Datatypes> parseDatatypes(File repoDir) throws JAXBException {
        return unmarshall(repoDir, Datatypes.class);
    }

    private static DataType toDataType(DatatypeCsT dt) {
        return new DataType(dt.getName(), dt.getBaseType(), dt.getDescription());
    }

    private Optional<Messages> parseMessages(File repoDir) throws JAXBException {
        return unmarshall(repoDir, Messages.class);
    }

    private static Message toMessage(MessageCsT m) {
        return new Message(m.getComponentID(), m.getMsgType(), m.getName(), m.getDescription());
    }

    private Optional<Components> parseComponents(File repoDir) throws JAXBException {
        return unmarshall(repoDir, Components.class);
    }

    private static Component toComponent(ComponentCsT c) {
        return new Component(c.getComponentID().intValue(), c.getComponentType().value(), c.getName(),
                c.getDescription());
    }

    private Optional<Fields> parseFields(File repoDir) throws JAXBException {
        return unmarshall(repoDir, Fields.class);
    }

    private static Field toField(FieldCsT f) {
        return new Field(f.getTag().intValue(), f.getName(), f.getType(), f.getDescription());
    }

    private Optional<MsgContents> parseMsgContents(File repoDir) throws JAXBException {
        return unmarshall(repoDir, MsgContents.class);
    }

    private static Content toContent(MsgContentCsT mc) {
        int ownerId = Integer.parseInt(mc.getComponentID());
        int position = mc.getPosition().intValue();
        boolean required = mc.getReqd() == 0;
        if (mc.getTagText().matches("\\d+")) {
            return new Content.FieldContent(ownerId, position, required, Integer.parseInt(mc.getTagText()));
        } else {
            return new Content.ComponentContent(ownerId, position, required, mc.getTagText());
        }
    }

    private <T> Optional<T> unmarshall(File repoDir, Class<T> unmarshallType) throws JAXBException {
        File xmlFile = new File(repoDir, unmarshallType.getSimpleName() + ".xml");
        if (!xmlFile.exists()) {
            return Optional.empty();
        }

        return Optional.of((T) unmarshaller.unmarshal(xmlFile));
    }
}
