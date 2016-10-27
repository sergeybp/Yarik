package Network.Messages.InitMessages;

import Network.Messages.YarikMessage;
import Network.Messages.YarikMessageField;
import Network.Messages.YarikMessageType;

/**
 * Created by sergeybp on 27.10.16.
 */
//This message INIT sends to Servers to ask about their load.
public class MessageAskServerLoad extends YarikMessage{
    @Override
    public YarikMessageType getMessageType() {
        return YarikMessageType.ASKESERVERLOAD;
    }

    @Override
    public YarikMessageField[] getMessageFields() {
        //TODO
        return new YarikMessageField[0];
    }
}
