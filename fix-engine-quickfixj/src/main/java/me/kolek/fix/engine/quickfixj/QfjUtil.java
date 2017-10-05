package me.kolek.fix.engine.quickfixj;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import me.kolek.fix.FixDictionary;
import me.kolek.fix.FixMessage;
import me.kolek.fix.TagValue;
import me.kolek.fix.engine.FixSessionId;
import me.kolek.fix.engine.config.FixSessionConfiguration;
import me.kolek.util.CollectionUtil;
import me.kolek.util.tuple.Tuple2;
import quickfix.*;

import java.util.ListIterator;

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

    public static Message toMessage(FixDictionary dictionary, FixMessage fixMessage) {
        Message message = new Message();

        for (ListIterator<TagValue> iter = fixMessage.listIterator(); iter.hasNext();) {
            TagValue tagValue = iter.next();
            if (QfjMessage.isHeaderField(tagValue.getTagNum())) {
                setField(dictionary, iter, tagValue, message.getHeader());
            } else if (QfjMessage.isTrailerField(tagValue.getTagNum())) {
                setField(dictionary, iter, tagValue, message.getTrailer());
            } else {
                setField(dictionary, iter, tagValue, message);
            }
        }

        return message;
    }

    private static void setField(FixDictionary dictionary, ListIterator<TagValue> iter, TagValue tagValue,
            FieldMap fields) {
        fields.setString(tagValue.getTagNum(), tagValue.getValue());

        if (dictionary.isGroup(tagValue.getTagNum())) {
            int groupTagNum = tagValue.getTagNum();
            int[] groupTagNums = dictionary.getTagNums(tagValue.getTagNum());
            int firstTagNum = groupTagNums[0];

            TIntSet _groupTagNums = new TIntHashSet(groupTagNums);
            Group group = new Group(groupTagNum, firstTagNum);

            while (iter.hasNext()) {
                tagValue = iter.next();
                if (!_groupTagNums.contains(tagValue.getTagNum())) {
                    iter.previous();
                    break;
                }
                if (group.isSetField(tagValue.getTagNum())) {
                    fields.addGroup(group);
                    group = new Group(groupTagNum, firstTagNum);
                }
                setField(dictionary, iter, tagValue, group);
            }

            fields.addGroup(group);
        }
    }
}
