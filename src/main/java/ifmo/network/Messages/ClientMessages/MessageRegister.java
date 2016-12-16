package ifmo.network.Messages.ClientMessages;

import ifmo.network.Messages.YarikMessage;
import ifmo.network.Messages.YarikMessageField;
import ifmo.network.Messages.YarikMessageType;

/**
 * Created by sergeybp on 21.11.16.
 */
public class MessageRegister extends YarikMessage {
    @Override
    public YarikMessageType getMessageType() {
        return YarikMessageType.REGISTER;
    }

    @Override
    public YarikMessageField[] getMessageFields() {
        return new YarikMessageField[]{YarikMessageField.NETWORKINFO, YarikMessageField.INFO, YarikMessageField.TAGS};
    }
}
