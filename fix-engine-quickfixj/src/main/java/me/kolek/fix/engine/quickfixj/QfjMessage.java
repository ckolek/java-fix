package me.kolek.fix.engine.quickfixj;

import me.kolek.fix.FixMessage;
import quickfix.InvalidMessage;
import quickfix.Message;

class QfjMessage extends Message {
    QfjMessage(FixMessage message) throws InvalidMessage {
        super(message.toString());
    }
}
