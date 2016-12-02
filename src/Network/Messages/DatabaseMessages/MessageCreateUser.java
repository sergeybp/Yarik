package Network.Messages.DatabaseMessages;

import Network.Messages.YarikMessage;
import Network.Messages.YarikMessageField;
import Network.Messages.YarikMessageType;

/**
 * Created by sergeybp on 21.11.16.
 */
public class MessageCreateUser extends YarikMessage {
    @Override
    public YarikMessageType getMessageType() {
        return YarikMessageType.CREATE_USER;
    }

    @Override
    public YarikMessageField[] getMessageFields() {
        return new YarikMessageField[]{YarikMessageField.NETWORKINFO, YarikMessageField.JOBID, YarikMessageField.INFO, YarikMessageField.TAGS};
    }
}
