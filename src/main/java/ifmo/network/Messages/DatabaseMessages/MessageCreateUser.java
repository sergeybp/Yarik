package ifmo.network.Messages.DatabaseMessages;

import ifmo.network.Messages.YarikMessage;
import ifmo.network.Messages.YarikMessageField;
import ifmo.network.Messages.YarikMessageType;

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
