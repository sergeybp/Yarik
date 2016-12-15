package ifmo.network.Messages.ClientMessages;

import ifmo.network.Messages.YarikMessage;
import ifmo.network.Messages.YarikMessageField;
import ifmo.network.Messages.YarikMessageType;

/**
 * Created by sergeybp on 22.09.16.
 */
public class MessageGet extends YarikMessage {
    @Override
    public YarikMessageType getMessageType() {
        return YarikMessageType.GET;
    }

    @Override
    public YarikMessageField[] getMessageFields() {
        return new YarikMessageField[]{YarikMessageField.NETWORKINFO,YarikMessageField.INFO, YarikMessageField.LASTREAD,YarikMessageField.PURESYSTEMMESSAGE};
    }
}
