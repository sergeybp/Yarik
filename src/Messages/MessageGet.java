package Messages;

/**
 * Created by sergeybp on 22.09.16.
 */
public class MessageGet extends YarikMessage {
    @Override
    public YarikMessageType getMessageType() {
        return YarikMessageType.GET;
    }

    @Override
    public YarikMessageField[] getMessageFields() {
        return new YarikMessageField[]{YarikMessageField.TAGS,YarikMessageField.FEEDBACK};
    }
}
