package ifmo.network.Messages.ClientMessages;

import ifmo.network.Messages.YarikMessage;
import ifmo.network.Messages.YarikMessageField;
import ifmo.network.Messages.YarikMessageType;

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
