package ifmo.network.Messages;

/**
 * Created by sergeybp on 22.09.16.
 */
public class MessageUnknown extends YarikMessage {
    @Override
    public YarikMessageType getMessageType() {
        return null;
    }

    @Override
    public YarikMessageField[] getMessageFields() {
        return new YarikMessageField[0];
    }
}
