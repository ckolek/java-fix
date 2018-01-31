package quickfix;

import me.kolek.fix.FixDictionary;
import me.kolek.fix.FixMessage;
import me.kolek.fix.constants.ApplVerId;
import me.kolek.fix.constants.BeginString;
import me.kolek.fix.util.FixMessageParser;

import java.util.Optional;

public class QfjMessage extends Message {
    private final FixDictionary dictionary;
    private final FixMessageParser.Pool parserPool;
    private final String beginString;
    private final String applVerId;
    private final String msgType;

    private FixMessage fixMessage;

    public QfjMessage(FixDictionary dictionary, FixMessageParser.Pool parserPool, String beginString, String applVerId, String msgType) {
        this.dictionary = dictionary;
        this.parserPool = parserPool;
        this.beginString = beginString;
        this.applVerId = applVerId;
        this.msgType = msgType;
    }

    @Override
    void parse(String messageData, DataDictionary sessionDataDictionary, DataDictionary applicationDataDictionary,
            boolean doValidation) throws InvalidMessage {
        BeginString beginString = BeginString.fromValue(this.beginString).flatMap(bs -> {
            if (bs.isTransport() && !dictionary.getVersion(bs).isAdminMessage(msgType)) {
                return ApplVerId.fromValue(applVerId).map(ApplVerId::getBeginString);
            }
            return Optional.of(bs);
        }).orElseThrow(() -> new InvalidMessage("invalid FIX version: " + this.beginString));

        FixMessageParser parser = parserPool.getParser(beginString);
        fixMessage = parser.parse(messageData);
    }

    public FixMessage getFixMessage() {
        return fixMessage;
    }

    public static boolean isHeaderField(int tagNum, DataDictionary dictionary) {
        return Message.isHeaderField(tagNum) || (dictionary != null && dictionary.isHeaderField(tagNum));
    }

    public static boolean isTrailerField(int tagNum, DataDictionary dictionary) {
        return Message.isHeaderField(tagNum) || (dictionary != null && dictionary.isHeaderField(tagNum));
    }
}
