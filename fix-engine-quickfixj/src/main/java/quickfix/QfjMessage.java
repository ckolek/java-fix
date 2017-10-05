package quickfix;

import me.kolek.fix.FixDictionary;
import me.kolek.fix.FixMessage;
import me.kolek.fix.constants.TagNum;
import me.kolek.fix.engine.FixDictionaryProvider;
import me.kolek.fix.engine.quickfixj.QfjUtil;
import me.kolek.fix.util.FixMessageParser;

import java.rmi.RemoteException;

public class QfjMessage extends Message {
    private final FixDictionaryProvider dictionaryProvider;
    private final String beginString;
    private final String msgType;

    private FixMessage fixMessage;

    public QfjMessage(FixDictionaryProvider dictionaryProvider, String beginString, String msgType) {
        this.dictionaryProvider = dictionaryProvider;
        this.beginString = beginString;
        this.msgType = msgType;
    }

    @Override
    void parse(String messageData, DataDictionary sessionDataDictionary, DataDictionary applicationDataDictionary,
            boolean doValidation) throws InvalidMessage {
        SessionID sessionId = MessageUtils.getReverseSessionID(messageData);
        String applVerId;
        if (beginString.startsWith("FIXT")) {
            applVerId = MessageUtils.getStringField(messageData, TagNum.ApplVerID);
        } else {
            applVerId = null;
        }

        FixDictionary dictionary;
        try {
            dictionary = dictionaryProvider.getDictionary(QfjUtil.toFixSessionId(sessionId), applVerId);
        } catch (RemoteException e) {
            throw new InvalidMessage(e.getMessage());
        }

        FixMessageParser parser = new FixMessageParser(dictionary);
        fixMessage = parser.parse(messageData);
    }

    public FixMessage getFixMessage() {
        return fixMessage;
    }

    public static boolean isHeaderField(int tagNum) {
        return Message.isHeaderField(tagNum);
    }

    public static boolean isTrailerField(int tagNum) {
        return Message.isTrailerField(tagNum);
    }
}
