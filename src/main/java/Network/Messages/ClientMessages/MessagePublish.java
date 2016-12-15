package Network.Messages.ClientMessages;

import Network.Messages.YarikMessage;
import Network.Messages.YarikMessageField;
import Network.Messages.YarikMessageType;

/**
 * Created by sergeybp on 22.09.16.
 */
public class MessagePublish extends YarikMessage {

    @Override
    public YarikMessageType getMessageType() {
        return YarikMessageType.PUBLISH;
    }

    @Override
    public YarikMessageField[] getMessageFields() {
        return new YarikMessageField[]{YarikMessageField.NETWORKINFO, YarikMessageField.INFO, YarikMessageField.MESSAGE,YarikMessageField.TAGS};
    }


}
