package Network.Messages.InitMessages;

import Network.Messages.YarikMessage;
import Network.Messages.YarikMessageField;
import Network.Messages.YarikMessageType;

/**
 * Created by sergeybp on 27.10.16.
 */
// This message INIT sends to Client to return Server address.
public class MessageReturnServerAddress extends YarikMessage{
    @Override
    public YarikMessageType getMessageType() {
        return YarikMessageType.RETURNSERVERADDRESS;
    }

    @Override
    public YarikMessageField[] getMessageFields() {
        return new YarikMessageField[]{YarikMessageField.PURESYSTEMMESSAGE};
    }
}
