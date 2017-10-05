package me.kolek.fix.engine.quickfixj;

import me.kolek.fix.engine.FixDictionaryProvider;
import quickfix.Group;
import quickfix.Message;
import quickfix.MessageFactory;
import quickfix.QfjMessage;

public class QfjMessageFactory implements MessageFactory {
    private final FixDictionaryProvider dictionaryProvider;

    public QfjMessageFactory(FixDictionaryProvider dictionaryProvider) {
        this.dictionaryProvider = dictionaryProvider;
    }

    @Override
    public Message create(String beginString, String msgType) {
        return new QfjMessage(dictionaryProvider, beginString, msgType);
    }

    @Override
    public Group create(String beginString, String msgType, int correspondingFieldID) {
        throw new UnsupportedOperationException("create quickfix.Group");
    }
}
