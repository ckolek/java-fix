package me.kolek.fix.engine.quickfixj;

import me.kolek.fix.engine.config.FixSessionConfiguration;
import me.kolek.util.CollectionUtil;
import me.kolek.util.tuple.Tuple2;
import quickfix.*;

public enum QfjUtil {
    ;

    public static SessionID toSessionId(FixSessionConfiguration sessionConfiguration) {
        return new SessionID(sessionConfiguration.getBeginString(), sessionConfiguration.getSenderCompId(),
                sessionConfiguration.getSenderSubId(), sessionConfiguration.getSenderLocationId(),
                sessionConfiguration.getTargetCompId(), sessionConfiguration.getTargetSubId(),
                sessionConfiguration.getTargetLocationId(), sessionConfiguration.getSessionId());
    }

    public static Dictionary toSessionSettings(FixSessionConfiguration sessionConfiguration) {
        Dictionary dictionary = new Dictionary();
        setString(dictionary, SessionSettings.BEGINSTRING, sessionConfiguration.getBeginString());
        setString(dictionary, SessionSettings.SENDERCOMPID, sessionConfiguration.getSenderCompId());
        setString(dictionary, SessionSettings.SENDERSUBID, sessionConfiguration.getSenderSubId());
        setString(dictionary, SessionSettings.SENDERLOCID, sessionConfiguration.getSenderLocationId());
        setString(dictionary, SessionSettings.TARGETCOMPID, sessionConfiguration.getTargetCompId());
        setString(dictionary, SessionSettings.TARGETSUBID, sessionConfiguration.getTargetSubId());
        setString(dictionary, SessionSettings.TARGETLOCID, sessionConfiguration.getTargetLocationId());
        setString(dictionary, SessionSettings.SESSION_QUALIFIER, sessionConfiguration.getSessionId());
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
}
