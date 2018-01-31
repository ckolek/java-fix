package me.kolek.fix.engine.quickfixj;

import me.kolek.fix.FixDictionary;
import me.kolek.fix.FixDictionary.Version;
import me.kolek.fix.FixMessage;
import me.kolek.fix.TagValue;
import me.kolek.fix.constants.ApplVerId;
import me.kolek.fix.constants.BeginString;
import me.kolek.fix.constants.TagNum;
import me.kolek.fix.engine.FixSessionId;
import me.kolek.fix.engine.InvalidMessageException;
import me.kolek.fix.engine.config.FixSessionConfiguration;
import me.kolek.util.XmlUtil;
import me.kolek.util.collection.CollectionUtil;
import me.kolek.util.tuple.Tuple2;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import quickfix.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.BiConsumer;

public enum QfjUtil {
    ;

    public static Dictionary toSessionSettings(FixSessionConfiguration sessionConfiguration) {
        Dictionary dictionary = new Dictionary();
        setString(dictionary, SessionSettings.BEGINSTRING, sessionConfiguration.getSessionId().getBeginString());
        setString(dictionary, SessionSettings.SENDERCOMPID, sessionConfiguration.getSessionId().getSenderCompId());
        setString(dictionary, SessionSettings.SENDERSUBID, sessionConfiguration.getSessionId().getSenderSubId());
        setString(dictionary, SessionSettings.SENDERLOCID, sessionConfiguration.getSessionId().getSenderLocationId());
        setString(dictionary, SessionSettings.TARGETCOMPID, sessionConfiguration.getSessionId().getTargetCompId());
        setString(dictionary, SessionSettings.TARGETSUBID, sessionConfiguration.getSessionId().getTargetSubId());
        setString(dictionary, SessionSettings.TARGETLOCID, sessionConfiguration.getSessionId().getTargetLocationId());
        setString(dictionary, Session.SETTING_DEFAULT_APPL_VER_ID, sessionConfiguration.getDefaultApplVerId());
        setString(dictionary, Session.SETTING_START_TIME, "00:00:00");
        setString(dictionary, Session.SETTING_END_TIME, "00:00:00");

        if (sessionConfiguration.isAcceptor()) {
            setString(dictionary, SessionFactory.SETTING_CONNECTION_TYPE, SessionFactory.ACCEPTOR_CONNECTION_TYPE);
            if (sessionConfiguration.getAddresses().size() == 1) {
                Tuple2<String, Integer> address = sessionConfiguration.getAddresses().get(0);
                setString(dictionary, Acceptor.SETTING_SOCKET_ACCEPT_ADDRESS, address.first());
                setLong(dictionary, Acceptor.SETTING_SOCKET_ACCEPT_PORT, address.second());
            } else if (sessionConfiguration.getAddresses().size() > 1) {
                CollectionUtil.indexed(sessionConfiguration.getAddresses()).forEach(indexAddress -> {
                    int index = indexAddress.first();
                    Tuple2<String, Integer> address = indexAddress.second();
                    setString(dictionary, Acceptor.SETTING_SOCKET_ACCEPT_ADDRESS + (index + 1), address.first());
                    setLong(dictionary, Acceptor.SETTING_SOCKET_ACCEPT_PORT + (index + 1), address.second());
                });
            }
        } else {
            setString(dictionary, SessionFactory.SETTING_CONNECTION_TYPE, SessionFactory.INITIATOR_CONNECTION_TYPE);
            Tuple2<String, Integer> address = sessionConfiguration.getAddresses().get(0);
            setString(dictionary, Initiator.SETTING_SOCKET_CONNECT_HOST, address.first());
            setLong(dictionary, Initiator.SETTING_SOCKET_CONNECT_PORT, address.second());
            setLong(dictionary, Session.SETTING_HEARTBTINT, sessionConfiguration.getHeartbeatInterval());
        }
        return dictionary;
    }

    public static void setString(Dictionary dictionary, String key, String value) {
        if (value != null) {
            dictionary.setString(key, value);
        }
    }

    public static void setLong(Dictionary dictionary, String key, Number value) {
        if (value != null) {
            dictionary.setString(key, value.toString());
        }
    }

    public static SessionID toSessionID(FixSessionId fixSessionId) {
        return new SessionID(fixSessionId.getBeginString(), fixSessionId.getSenderCompId(),
                fixSessionId.getSenderSubId(), fixSessionId.getSenderLocationId(), fixSessionId.getTargetCompId(),
                fixSessionId.getTargetSubId(), fixSessionId.getTargetLocationId(), null);
    }

    public static FixSessionId toFixSessionId(SessionID sessionID) {
        return new FixSessionId(sessionID.getBeginString(), sessionID.getSenderCompID(), sessionID.getSenderSubID(),
                sessionID.getSenderLocationID(), sessionID.getTargetCompID(), sessionID.getTargetSubID(),
                sessionID.getTargetLocationID());
    }

    public static Map<BeginString, DataDictionary> toDataDictionaries(FixDictionary dictionary) throws ConfigError {
        Map<BeginString, DataDictionary> dataDictionaries = new HashMap<>();
        for (Version version : dictionary.getVersions()) {
            dataDictionaries.put(version.getBeginString(), toDataDictionary(version));
        }
        return dataDictionaries;
    }

    public static DataDictionary toDataDictionary(Version dictionary) throws ConfigError {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element fixElement = XmlUtil.appendElement(document, "fix");
            Element headerElement = XmlUtil.appendElement(fixElement, "header");
            Element trailerElement = XmlUtil.appendElement(fixElement, "trailer");
            Element messagesElement = XmlUtil.appendElement(fixElement, "messages");
            Element componentsElement = XmlUtil.appendElement(fixElement, "components");
            Element fieldsElement = XmlUtil.appendElement(fixElement, "fields");

            BiConsumer<Element, Version.Element> appendElement = (el, e) -> {
                String child;
                switch (e.getType()) {
                    case COMPONENT:
                        child = "component";
                        break;
                    case GROUP:
                        child = "group";
                        break;
                    case FIELD:
                        child = "field";
                        break;
                    default:
                        return;
                }
                XmlUtil.appendElement(el, child,
                        a -> a.put("name", e.getName()).put("required", e.isRequired() ? "Y" : "N"));
            };

            for (Version.Message message : dictionary.getMessages()) {
                Element messageElement = XmlUtil.appendElement(messagesElement, "message",
                        a -> a.put("name", message.getName()).put("msgtype", message.getMsgType()));
                message.getElements().forEach(element -> appendElement.accept(messageElement, element));
            }

            for (Version.Component component : dictionary.getComponents()) {
                Element componentElement;
                switch (component.getName()) {
                    case "StandardHeader":
                        componentElement = headerElement;
                        break;
                    case "StandardTrailer":
                        componentElement = trailerElement;
                        break;
                    default:
                        componentElement = XmlUtil.appendElement(componentsElement, "component",
                                a -> a.put("name", component.getName()));
                }
                component.getElements().forEach(element -> appendElement.accept(componentElement, element));
            }

            for (Version.Group group : dictionary.getGroups()) {
                Version.Field numInGroupField = dictionary.getField(group.getNumInGroupTagNum());
                Element componentElement =
                        XmlUtil.appendElement(componentsElement, "group", a -> a.put("name", group.getName()));
                Element groupElement = XmlUtil.appendElement(componentElement, "group",
                        a -> a.put("name", numInGroupField.getName()).put("required", "N"));
                group.getElements().forEach(element -> appendElement.accept(groupElement, element));
            }

            for (Version.Field field : dictionary.getFields()) {
                XmlUtil.appendElement(fieldsElement, "field",
                        a -> a.put("number", field.getTagNum()).put("name", field.getName())
                                .put("type", QfjUtil.toFieldType(field.getType())));
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            transformer.transform(new DOMSource(document), new StreamResult(outputStream));

            return new DataDictionary(new ByteArrayInputStream(outputStream.toByteArray()));
        } catch (TransformerException | ParserConfigurationException e) {
            throw new ConfigError(e);
        }
    }

    public static Message toMessage(Map<BeginString, DataDictionary> dictionaries, FixMessage fixMessage,
            String defApplVerId) throws InvalidMessageException {
        String msgType = fixMessage.getMsgType().orElseThrow(() -> new InvalidMessageException("no MsgType field"));
        DataDictionary dictionary = fixMessage.getBeginString().flatMap(BeginString::fromValue).map(beginString -> {
            DataDictionary _dictionary = dictionaries.get(beginString);
            if (beginString.isTransport() && _dictionary.isAppMessage(msgType)) {
                _dictionary = ApplVerId.fromValue(fixMessage.getValue(TagNum.ApplVerID).orElse(defApplVerId))
                        .map(ApplVerId::getBeginString).map(dictionaries::get).orElse(null);
            }
            return _dictionary;
        }).orElse(null);
        return toMessage(dictionary, fixMessage);
    }

    public static Message toMessage(DataDictionary dictionary, FixMessage fixMessage) throws InvalidMessageException {
        if (dictionary == null) {
            try {
                return new Message(fixMessage.toString());
            } catch (InvalidMessage e) {
                throw new InvalidMessageException(e.getMessage());
            }
        }

        String msgType = fixMessage.getMsgType().orElseThrow(() -> new InvalidMessageException("no MsgType field"));

        Message message = new Message();

        for (ListIterator<TagValue> iter = fixMessage.listIterator(); iter.hasNext();) {
            TagValue tagValue = iter.next();
            if (QfjMessage.isHeaderField(tagValue.getTagNum(), dictionary)) {
                setField(dictionary, iter, tagValue, msgType, message.getHeader());
            } else if (QfjMessage.isTrailerField(tagValue.getTagNum(), dictionary)) {
                setField(dictionary, iter, tagValue, msgType, message.getTrailer());
            } else {
                setField(dictionary, iter, tagValue, msgType, message);
            }
        }

        return message;
    }

    private static void setField(DataDictionary dictionary, ListIterator<TagValue> iter, TagValue tagValue,
            String msgType, FieldMap fields) {
        fields.setString(tagValue.getTagNum(), tagValue.getValue());

        if (dictionary.isGroup(msgType, tagValue.getTagNum())) {
            int groupTagNum = tagValue.getTagNum();
            DataDictionary groupDD = dictionary.getGroup(msgType, tagValue.getTagNum()).getDataDictionary();
            int[] groupTagNums = groupDD.getOrderedFields();
            int firstTagNum = groupTagNums[0];

            Group qfjGroup = new Group(groupTagNum, firstTagNum);

            while (iter.hasNext()) {
                tagValue = iter.next();
                if (!groupDD.isField(tagValue.getTagNum())) {
                    iter.previous();
                    break;
                }
                if (qfjGroup.isSetField(tagValue.getTagNum())) {
                    fields.addGroup(qfjGroup);
                    qfjGroup = new Group(groupTagNum, firstTagNum);
                }
                setField(dictionary, iter, tagValue, msgType, qfjGroup);
            }
            if (!qfjGroup.isEmpty()) {
                fields.addGroup(qfjGroup);
            }
        }
    }

    public static me.kolek.fix.constants.FieldType toFieldType(FieldType fieldType) {
        switch (fieldType) {
            case AMT:
                return me.kolek.fix.constants.FieldType.AMT;
            case BOOLEAN:
                return me.kolek.fix.constants.FieldType.BOOLEAN;
            case CHAR:
                return me.kolek.fix.constants.FieldType.CHAR;
            case COUNTRY:
                return me.kolek.fix.constants.FieldType.COUNTRY;
            case CURRENCY:
                return me.kolek.fix.constants.FieldType.CURRENCY;
            case DATA:
                return me.kolek.fix.constants.FieldType.DATA;
            case DAYOFMONTH:
                return me.kolek.fix.constants.FieldType.DAYOFMONTH;
            case EXCHANGE:
                return me.kolek.fix.constants.FieldType.EXCHANGE;
            case FLOAT:
                return me.kolek.fix.constants.FieldType.FLOAT;
            case INT:
                return me.kolek.fix.constants.FieldType.INT;
            case LENGTH:
                return me.kolek.fix.constants.FieldType.LENGTH;
            case LOCALMKTDATE:
                return me.kolek.fix.constants.FieldType.LOCALMKTDATE;
            case MONTHYEAR:
                return me.kolek.fix.constants.FieldType.MONTHYEAR;
            case MULTIPLESTRINGVALUE:
                return me.kolek.fix.constants.FieldType.MULTIPLECHARVALUE;
            case MULTIPLEVALUESTRING:
                return me.kolek.fix.constants.FieldType.MULTIPLESTRINGVALUE;
            case NUMINGROUP:
                return me.kolek.fix.constants.FieldType.NUMINGROUP;
            case PERCENTAGE:
                return me.kolek.fix.constants.FieldType.PERCENTAGE;
            case PRICE:
                return me.kolek.fix.constants.FieldType.PRICE;
            case PRICEOFFSET:
                return me.kolek.fix.constants.FieldType.PRICEOFFSET;
            case QTY:
                return me.kolek.fix.constants.FieldType.QTY;
            case SEQNUM:
                return me.kolek.fix.constants.FieldType.SEQNUM;
            case STRING:
                return me.kolek.fix.constants.FieldType.STRING;
            case TIME:
                return me.kolek.fix.constants.FieldType.TZTIMEONLY;
            case UTCDATE:
                return me.kolek.fix.constants.FieldType.TZTIMESTAMP;
            case UTCDATEONLY:
                return me.kolek.fix.constants.FieldType.UTCDATEONLY;
            case UTCTIMEONLY:
                return me.kolek.fix.constants.FieldType.UTCTIMEONLY;
            case UTCTIMESTAMP:
                return me.kolek.fix.constants.FieldType.UTCTIMESTAMP;
            case UNKNOWN:
            default:
                throw new IllegalArgumentException("cannot get FieldType for type UNKNOWN");
        }
    }

    public static FieldType toFieldType(me.kolek.fix.constants.FieldType fieldType) {
        switch (fieldType) {
            case AMT:
                return FieldType.AMT;
            case BOOLEAN:
                return FieldType.BOOLEAN;
            case CHAR:
                return FieldType.CHAR;
            case COUNTRY:
                return FieldType.COUNTRY;
            case CURRENCY:
                return FieldType.CURRENCY;
            case DATA:
                return FieldType.DATA;
            case DAYOFMONTH:
                return FieldType.DAYOFMONTH;
            case EXCHANGE:
                return FieldType.EXCHANGE;
            case FLOAT:
                return FieldType.FLOAT;
            case INT:
                return FieldType.INT;
            case LENGTH:
                return FieldType.LENGTH;
            case LOCALMKTDATE:
                return FieldType.LOCALMKTDATE;
            case MONTHYEAR:
                return FieldType.MONTHYEAR;
            case MULTIPLECHARVALUE:
                return FieldType.MULTIPLEVALUESTRING;
            case MULTIPLESTRINGVALUE:
                return FieldType.MULTIPLESTRINGVALUE;
            case NUMINGROUP:
                return FieldType.NUMINGROUP;
            case PERCENTAGE:
                return FieldType.PERCENTAGE;
            case PRICE:
                return FieldType.PRICE;
            case PRICEOFFSET:
                return FieldType.PRICEOFFSET;
            case QTY:
                return FieldType.QTY;
            case SEQNUM:
                return FieldType.SEQNUM;
            case STRING:
                return FieldType.STRING;
            case TZTIMEONLY:
                return FieldType.TIME;
            case TZTIMESTAMP:
                return FieldType.UTCDATE;
            case UTCDATEONLY:
                return FieldType.UTCDATEONLY;
            case UTCTIMEONLY:
                return FieldType.UTCTIMEONLY;
            case UTCTIMESTAMP:
                return FieldType.UTCTIMESTAMP;
            default:
                return FieldType.UNKNOWN;
        }
    }
}
