package ifmo.network.Messages.ClientMessages;

import ifmo.network.Messages.YarikMessage;
import ifmo.network.Messages.YarikMessageField;
import ifmo.network.Messages.YarikMessageType;

/**
 * Created by sergeybp on 14.12.16.
 */
public class MessageFeedback extends YarikMessage {

    @Override
    public YarikMessageType getMessageType() {
        return YarikMessageType.FEEDBACK;
    }

    @Override
    public YarikMessageField[] getMessageFields() {
        return new YarikMessageField[]{YarikMessageField.NETWORKINFO,YarikMessageField.INFO,YarikMessageField.PURESYSTEMMESSAGE};
    }
}
