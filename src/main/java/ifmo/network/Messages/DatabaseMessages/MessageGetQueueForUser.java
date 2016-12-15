package ifmo.network.Messages.DatabaseMessages;

import ifmo.network.Messages.YarikMessage;
import ifmo.network.Messages.YarikMessageField;
import ifmo.network.Messages.YarikMessageType;

/**
 * Created by sergeybp on 16.11.16.
 */
public class MessageGetQueueForUser extends YarikMessage {
    @Override
    public YarikMessageType getMessageType() {
        return YarikMessageType.GET_QUEUE_FOR_USER;
    }

    @Override
    public YarikMessageField[] getMessageFields() {
        return new YarikMessageField[]{YarikMessageField.NETWORKINFO,YarikMessageField.JOBID, YarikMessageField.INFO,YarikMessageField.LASTREAD,YarikMessageField.PURESYSTEMMESSAGE};
    }
}
