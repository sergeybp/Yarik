package Network.Messages.DatabaseMessages;

import Network.Messages.YarikMessage;
import Network.Messages.YarikMessageField;
import Network.Messages.YarikMessageType;

/**
 * Created by sergeybp on 16.11.16.
 */
public class MessageGetQueueForUser extends YarikMessage {
    @Override
    public YarikMessageType getMessageType() {
        return YarikMessageType.GETQUEUEFORUSER;
    }

    @Override
    public YarikMessageField[] getMessageFields() {
        return new YarikMessageField[]{YarikMessageField.NETWORKINFO,YarikMessageField.JOBID, YarikMessageField.INFO,YarikMessageField.LASTREAD,YarikMessageField.PURESYSTEMMESSAGE};
    }
}
