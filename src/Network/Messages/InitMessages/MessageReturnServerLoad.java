package Network.Messages.InitMessages;

import Network.Messages.YarikMessage;
import Network.Messages.YarikMessageField;
import Network.Messages.YarikMessageType;

/**
 * Created by sergeybp on 27.10.16.
 */
// This message Server sends to INIT to answer about his load.
public class MessageReturnServerLoad extends YarikMessage{
    @Override
    public YarikMessageType getMessageType() {
        return YarikMessageType.RETURNSERVERLOAD;
    }

    @Override
    public YarikMessageField[] getMessageFields() {
        //TODO
        return new YarikMessageField[0];
    }
}
