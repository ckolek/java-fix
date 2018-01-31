package me.kolek.fix.engine.quickfixj;

import me.kolek.fix.FixDictionary;
import me.kolek.fix.util.FixMessageParser;
import quickfix.Group;
import quickfix.Message;
import quickfix.MessageFactory;
import quickfix.QfjMessage;
import quickfix.field.ApplVerID;

class QfjMessageFactory implements MessageFactory {
    private final FixDictionary dictionary;
    private final FixMessageParser.Pool parserPool;

    QfjMessageFactory(FixDictionary dictionary) {
        this.dictionary = dictionary;
        this.parserPool = new FixMessageParser.Pool(dictionary);
    }

    @Override
    public Message create(String beginString, String msgType) {
        return create(beginString, null, msgType);
    }

    @Override
    public Message create(String beginString, ApplVerID applVerID, String msgType) {
        String applVerId = applVerID != null ? applVerID.getValue() : null;
        return new QfjMessage(dictionary, parserPool, beginString, applVerId, msgType);
    }

    @Override
    public Group create(String beginString, String msgType, int correspondingFieldID) {
        throw new UnsupportedOperationException("create quickfix.Group");
    }
}
