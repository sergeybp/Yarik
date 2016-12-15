package ifmo.network.Messages.DatabaseMessages;

import ifmo.network.Messages.YarikMessage;
import ifmo.network.Messages.YarikMessageField;
import ifmo.network.Messages.YarikMessageType;

/**
 * Created by sergeybp on 14.12.16.
 */
public class MessageFeedbackDB extends YarikMessage {

    @Override
    public YarikMessageType getMessageType() {
        return YarikMessageType.FEEDBACK_DB;
    }

    @Override
    public YarikMessageField[] getMessageFields() {
        return new YarikMessageField[]{YarikMessageField.NETWORKINFO,YarikMessageField.JOBID,YarikMessageField.PURESYSTEMMESSAGE};
    }
}
