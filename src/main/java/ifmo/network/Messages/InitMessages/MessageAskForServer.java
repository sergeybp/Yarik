package ifmo.network.Messages.InitMessages;

import ifmo.network.Messages.YarikMessage;
import ifmo.network.Messages.YarikMessageField;
import ifmo.network.Messages.YarikMessageType;

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
        return new YarikMessageField[]{YarikMessageField.NETWORKINFO, YarikMessageField.PURESYSTEMMESSAGE};
    }
}
