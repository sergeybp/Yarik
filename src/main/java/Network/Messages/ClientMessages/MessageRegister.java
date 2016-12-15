package Network.Messages.ClientMessages;

import Network.Messages.YarikMessage;
import Network.Messages.YarikMessageField;
import Network.Messages.YarikMessageType;

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
