package ifmo.network.Messages.InitMessages;

import ifmo.network.Messages.YarikMessage;
import ifmo.network.Messages.YarikMessageField;
import ifmo.network.Messages.YarikMessageType;

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
        return new YarikMessageField[]{YarikMessageField.NETWORKINFO,YarikMessageField.PURESYSTEMMESSAGE};
    }
}
