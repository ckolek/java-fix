package me.kolek.fix.engine.quickfixj;

import me.kolek.fix.FixDictionary;
import me.kolek.fix.constants.ApplVerId;
import me.kolek.fix.constants.BeginString;
import me.kolek.fix.util.FixMessageParser;
import quickfix.*;

class QfjMessageFactory implements MessageFactory {
    private final FixDictionary dictionary;
    private final FixMessageParser.Pool parserPool;

    QfjMessageFactory(FixDictionary dictionary) {
        this.dictionary = dictionary;
        this.parserPool = new FixMessageParser.Pool(dictionary);
    }

    @Override
    public Message create(String beginString, String msgType) {
        String applVerId = BeginString.fromValue(beginString).map(bs -> bs.isTransport() ? ApplVerId.FIX50 : null)
                .map(ApplVerId::value).orElse(null);
        return new QfjMessage(dictionary, parserPool, beginString, applVerId, msgType);
    }

    @Override
    public Group create(String beginString, String msgType, int correspondingFieldID) {
        throw new UnsupportedOperationException("create quickfix.Group");
    }
}
