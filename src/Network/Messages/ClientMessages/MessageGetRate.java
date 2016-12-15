package Network.Messages.ClientMessages;

import Network.Messages.YarikMessage;
import Network.Messages.YarikMessageField;
import Network.Messages.YarikMessageType;

/**
 * Created by sergeybp on 14.12.16.
 */
public class MessageGetRate extends YarikMessage {

    @Override
    public YarikMessageType getMessageType() {
        return YarikMessageType.GET_RATE;
    }

    @Override
    public YarikMessageField[] getMessageFields() {
        return new YarikMessageField[]{YarikMessageField.NETWORKINFO,YarikMessageField.INFO,YarikMessageField.PURESYSTEMMESSAGE};
    }
}
