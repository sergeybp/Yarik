package Network.Messages.ClientMessages;

import Network.Messages.YarikMessage;
import Network.Messages.YarikMessageField;
import Network.Messages.YarikMessageType;

/**
 * Created by sergeybp on 22.09.16.
 */
public class MessageManage extends YarikMessage {
    @Override
    public YarikMessageType getMessageType() {
        return YarikMessageType.MANAGE;
    }

    @Override
    public YarikMessageField[] getMessageFields() {
        return new YarikMessageField[]{YarikMessageField.FUNCTION, YarikMessageField.ARGS, YarikMessageField.FEEDBACK};
    }
}
