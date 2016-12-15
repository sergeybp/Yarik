package ifmo.network.Messages;

/**
 * Created by nikita on 15.12.16.
 */
public class YarikMessageException extends RuntimeException {
    public YarikMessageException() {
        super();
    }

    public YarikMessageException(String message) {
        super(message);
    }

    public YarikMessageException(Throwable throwable) {
        super(throwable);
    }

    public YarikMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
