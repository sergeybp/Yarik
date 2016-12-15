package AbstractServer;

/**
 * Created by nikita on 15.12.16.
 */
public class ServerException extends RuntimeException {
    public ServerException() {
        super();
    }

    public ServerException(String message) {
        super(message);
    }

    public ServerException(Throwable throwable) {
        super(throwable);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
