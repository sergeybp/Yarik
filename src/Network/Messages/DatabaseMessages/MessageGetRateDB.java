package Network.Messages.DatabaseMessages;

import Network.Messages.YarikMessage;
import Network.Messages.YarikMessageField;
import Network.Messages.YarikMessageType;

/**
 * Created by sergeybp on 14.12.16.
 */
public class MessageGetRateDB extends YarikMessage {

    @Override
    public YarikMessageType getMessageType() {
        return YarikMessageType.GET_RATEDB;
    }

    @Override
    public YarikMessageField[] getMessageFields() {
        return new YarikMessageField[]{YarikMessageField.NETWORKINFO,YarikMessageField.JOBID,YarikMessageField.INFO};
    }
}
