package Messages;

import Messages.YarikMessageField.*;

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
        return new YarikMessageField[]{YarikMessageField.MESSAGE,YarikMessageField.TAGS, YarikMessageField.FEEDBACK};
    }


}
