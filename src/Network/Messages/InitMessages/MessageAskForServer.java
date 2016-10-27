package Network.Messages.InitMessages;

import Network.Messages.YarikMessage;
import Network.Messages.YarikMessageField;
import Network.Messages.YarikMessageType;

/**
 * Created by sergeybp on 27.10.16.
 */

//This message client sends to INIT to get Server address.
public class MessageAskForServer extends YarikMessage {
    @Override
    public YarikMessageType getMessageType() {
        return YarikMessageType.ASKFORSERVER;
    }

    @Override
    public YarikMessageField[] getMessageFields() {
        return new YarikMessageField[]{YarikMessageField.NETWORKINFO};
    }
}
