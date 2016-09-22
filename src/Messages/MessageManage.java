package Messages;

/**
 * Created by sergeybp on 22.09.16.
 */
public class MessageManage extends YarikMessage {
    @Override
    public YarikMessageType getMessageType() {
        return YarikMessageType.MANAGE;
    }

    @Override
    public YarikMessageField[] getMessageFields() {
        return new YarikMessageField[]{YarikMessageField.FUNCTION, YarikMessageField.ARGS, YarikMessageField.FEEDBACK};
    }
}
